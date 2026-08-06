package com.example.prediction.builtin

import android.content.Context
import java.io.IOException

/**
 * Read-only query API over the shipped DAWG assets (dictionary_en.dawg / dictionary_bn.dawg).
 * Never mixed with personal/learned data — the builtin dictionary is a public, immutable asset.
 */
class BuiltInDictionaryRepository {

    private var english: DawgIndex? = null
    private var bangla: DawgIndex? = null

    var englishLoadMs: Long = 0
        private set
    var banglaLoadMs: Long = 0
        private set

    val isLoaded: Boolean get() = english != null || bangla != null

    fun load(context: Context) {
        loadAsset(context, "dictionary_en.dawg")?.let { (ms, index) ->
            englishLoadMs = ms
            english = index
        }
        loadAsset(context, "dictionary_bn.dawg")?.let { (ms, index) ->
            banglaLoadMs = ms
            bangla = index
        }
    }

    fun loadFromBytes(englishBytes: ByteArray?, banglaBytes: ByteArray?) {
        english = englishBytes?.let { measure { DawgIndex().apply { load(it) } } }?.second
        bangla = banglaBytes?.let { measure { DawgIndex().apply { load(it) } } }?.second
    }

    private fun loadAsset(context: Context, name: String): Pair<Long, DawgIndex>? {
        return try {
            val bytes = context.assets.open(name).use { it.readBytes() }
            val (ms, index) = measure { DawgIndex().apply { load(bytes) } }
            ms to index
        } catch (e: IOException) {
            null
        }
    }

    private fun <T> measure(block: () -> T): Pair<Long, T> {
        val start = System.nanoTime()
        val result = block()
        return (System.nanoTime() - start) / 1_000_000 to result
    }

    fun index(isBangla: Boolean): DawgIndex? = if (isBangla) bangla else english

    fun frequency(word: String, isBangla: Boolean): Int = index(isBangla)?.frequency(word) ?: 0

    fun contains(word: String, isBangla: Boolean): Boolean = frequency(word, isBangla) > 0

    fun topSuggestions(prefix: String, isBangla: Boolean, limit: Int): List<Pair<String, Int>> {
        val index = index(isBangla) ?: return emptyList()
        var state = index.rootState
        for (ch in prefix) {
            state = index.child(state, ch)
            if (state < 0) return emptyList()
        }
        return index.collectTop(state, prefix, limit)
    }

    fun topWords(isBangla: Boolean, limit: Int): List<Pair<String, Int>> =
        index(isBangla)?.topWords(limit) ?: emptyList()
}
