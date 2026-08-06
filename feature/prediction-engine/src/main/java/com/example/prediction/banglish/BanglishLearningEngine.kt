package com.example.prediction.banglish

import com.example.prediction.personal.PersonalTrieIndex

/**
 * Banglish-specific rules. Near-variant spellings the user consistently types
 * ("korbo" vs "korteci" vs "korsi") are distinct learned words — the typo-correction
 * engine must NEVER rewrite one into another. A token is protected once its personal
 * frequency reaches the learning threshold; only then does it "stand on its own".
 */
class BanglishLearningEngine(
    private val personalTrie: PersonalTrieIndex,
    private val threshold: () -> Int
) {

    /**
     * True when [token] is a confidently learned personal word that must not be
     * typo-corrected into a different form.
     */
    fun isProtected(token: String): Boolean {
        val entry = personalTrie.get(token.trim().lowercase()) ?: return false
        return entry.frequency >= threshold()
    }
}
