package com.example.clipboard

import android.content.ClipboardManager.OnPrimaryClipChangedListener
import android.content.Context
import com.example.data.AppDatabase
import com.example.data.ClipEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ClipItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isPinned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

object ClipboardManager {

    private const val MAX_UNPINNED = 100

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var database: AppDatabase? = null
    private var incognito: Boolean = false
    private var clipboardListener: OnPrimaryClipChangedListener? = null
    private var systemClipboard: android.content.ClipboardManager? = null

    private val _clips = MutableStateFlow<List<ClipItem>>(emptyList())
    val clips: StateFlow<List<ClipItem>> = _clips.asStateFlow()

    private var initialized = false

    fun init(context: Context) {
        if (initialized) return
        initialized = true
        database = AppDatabase.getInstance(context)
        systemClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        loadClips()
        startClipboardListener(context)
        startExpiryLoop()
    }

    fun setIncognito(enabled: Boolean) {
        incognito = enabled
        if (enabled) {
            stopClipboardListener()
        } else {
            context?.let { startClipboardListener(it) }
        }
    }

    private var context: Context? = null

    private fun startClipboardListener(ctx: Context) {
        if (incognito || clipboardListener != null) return
        val appContext = ctx.applicationContext
        context = appContext
        clipboardListener = OnPrimaryClipChangedListener {
            val clip = systemClipboard?.primaryClip ?: return@OnPrimaryClipChangedListener
            if (clip.itemCount > 0) {
                val text = clip.getItemAt(0).coerceToText(appContext)?.toString() ?: return@OnPrimaryClipChangedListener
                addClip(text)
            }
        }
        systemClipboard?.addPrimaryClipChangedListener(clipboardListener)
    }

    private fun stopClipboardListener() {
        clipboardListener?.let { systemClipboard?.removePrimaryClipChangedListener(it) }
        clipboardListener = null
    }

    fun isIncognito(): Boolean = incognito

    private fun loadClips() {
        scope.launch {
            val entities = database?.clipDao()?.getAllClips() ?: emptyList()
            _clips.value = entities.map { it.toClipItem() }
        }
    }

    fun setExpiryMinutes(minutes: Int) {
        expiryMinutes = minutes
    }

    private var expiryMinutes: Int = 0

    fun addClip(text: String) {
        if (incognito) return
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return

        scope.launch {
            val existing = database?.clipDao()?.findClipByText(trimmed)
            if (existing != null) {
                database?.clipDao()?.insertClip(existing.copy(timestamp = System.currentTimeMillis()))
            } else {
                database?.clipDao()?.insertClip(ClipEntity(text = trimmed))
            }

            cleanupExpiredClips()

            val all = database?.clipDao()?.getAllClips() ?: emptyList()
            val unpinned = all.filter { !it.isPinned }
            if (unpinned.size > MAX_UNPINNED) {
                unpinned.sortedBy { it.timestamp }.take(unpinned.size - MAX_UNPINNED).forEach {
                    database?.clipDao()?.deleteClip(it.id)
                }
            }
            loadClips()
        }
    }

    private suspend fun cleanupExpiredClips() {
        if (expiryMinutes <= 0) return
        val all = database?.clipDao()?.getAllClips() ?: emptyList()
        val now = System.currentTimeMillis()
        val expiryMs = expiryMinutes * 60 * 1000L
        all.filter { !it.isPinned && (now - it.timestamp) > expiryMs }.forEach {
            database?.clipDao()?.deleteClip(it.id)
        }
    }

    private fun startExpiryLoop() {
        scope.launch {
            while (true) {
                delay(60_000)
                if (expiryMinutes > 0) {
                    scope.launch { cleanupExpiredClips(); loadClips() }
                }
            }
        }
    }

    fun togglePin(id: String) {
        scope.launch {
            database?.clipDao()?.togglePin(id)
            loadClips()
        }
    }

    fun deleteClip(id: String) {
        scope.launch {
            database?.clipDao()?.deleteClip(id)
            loadClips()
        }
    }

    fun clearAllUnpinned() {
        scope.launch {
            database?.clipDao()?.clearAllUnpinned()
            loadClips()
        }
    }

    fun getClips(): List<ClipItem> = _clips.value

    private fun ClipEntity.toClipItem() = ClipItem(id, text, isPinned, timestamp)
}
