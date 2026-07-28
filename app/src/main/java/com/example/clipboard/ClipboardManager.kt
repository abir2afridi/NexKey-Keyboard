package com.example.clipboard

import android.content.Context
import com.example.data.AppDatabase
import com.example.data.ClipEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var database: AppDatabase? = null
    private var incognito: Boolean = false

    private val _clips = MutableStateFlow<List<ClipItem>>(emptyList())
    val clips: StateFlow<List<ClipItem>> = _clips.asStateFlow()

    fun init(context: Context) {
        database = AppDatabase.getInstance(context)
        loadClips()
    }

    fun setIncognito(enabled: Boolean) {
        incognito = enabled
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

    private var expiryMinutes: Int = 120

    fun addClip(text: String) {
        if (incognito) return
        val trimmed = text.trim()
        if (trimmed.isEmpty() || trimmed.length < 3) return

        scope.launch {
            val existing = database?.clipDao()?.findClipByText(trimmed)
            if (existing != null) {
                database?.clipDao()?.insertClip(existing.copy(timestamp = System.currentTimeMillis()))
            } else {
                database?.clipDao()?.insertClip(ClipEntity(text = trimmed))
            }

            val all = database?.clipDao()?.getAllClips() ?: emptyList()
            val now = System.currentTimeMillis()
            val expiryMs = expiryMinutes * 60 * 1000L
            all.filter { !it.isPinned && (now - it.timestamp) > expiryMs }.forEach {
                database?.clipDao()?.deleteClip(it.id)
            }
            val unpinned = all.filter { !it.isPinned }
            if (unpinned.size > 50) {
                unpinned.takeLast(unpinned.size - 50).forEach {
                    database?.clipDao()?.deleteClip(it.id)
                }
            }
            loadClips()
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
