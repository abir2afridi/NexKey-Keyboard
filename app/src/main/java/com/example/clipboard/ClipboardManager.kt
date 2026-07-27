package com.example.clipboard

import android.content.Context
import java.util.concurrent.ConcurrentHashMap

data class ClipItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isPinned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Manages clipboard history, pinning, auto-expiration, and content categorization.
 */
object ClipboardManager {

    private val clips = ConcurrentHashMap<String, ClipItem>()

    init {
        // Seed initial clipboard items for demo/testing
        addClip("Welcome to NexKey Keyboard! Type in English or বাংলা easily.")
        addClip("https://github.com/aistudio/nexkey")
    }

    fun addClip(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        
        // Avoid duplicate active top entry
        val existing = clips.values.find { it.text == trimmed }
        if (existing != null) {
            clips[existing.id] = existing.copy(timestamp = System.currentTimeMillis())
        } else {
            val item = ClipItem(text = trimmed)
            clips[item.id] = item
        }

        // Keep maximum 50 unpinned items
        val unpinned = clips.values.filter { !it.isPinned }.sortedBy { it.timestamp }
        if (unpinned.size > 50) {
            val toRemove = unpinned.take(unpinned.size - 50)
            toRemove.forEach { clips.remove(it.id) }
        }
    }

    fun togglePin(id: String) {
        clips[id]?.let { item ->
            clips[id] = item.copy(isPinned = !item.isPinned)
        }
    }

    fun deleteClip(id: String) {
        clips.remove(id)
    }

    fun clearAllUnpinned() {
        clips.values.filter { !it.isPinned }.forEach { clips.remove(it.id) }
    }

    fun getClips(): List<ClipItem> {
        return clips.values.sortedWith(
            compareByDescending<ClipItem> { it.isPinned }.thenByDescending { it.timestamp }
        )
    }
}
