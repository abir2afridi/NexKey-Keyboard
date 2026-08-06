package com.example.prediction.engine

import com.example.prediction.FeatureFlags
import com.example.prediction.PredictionProvider
import com.example.prediction.banglish.ScriptIntentDetector
import com.example.prediction.correction.AutoCorrectionEngine
import kotlinx.coroutines.flow.first

/**
 * The ONE place text gets auto-rewritten on word-boundary commit. The autocorrect
 * toggle is checked here as a single gate; suggestion-strip population and learning
 * are NOT gated by it (the user always sees suggestions and can tap one manually).
 */
class WordBoundaryCommitHandler(
    private val flags: FeatureFlags,
    private val provider: PredictionProvider,
    private val autoCorrection: AutoCorrectionEngine,
    private val detector: ScriptIntentDetector
) {

    data class CommitOutcome(
        val committed: String,
        val original: String,
        val autoCorrected: Boolean,
        val nextWords: List<PredictionCandidate>
    )

    suspend fun handleCommit(
        token: String,
        isBangla: Boolean,
        previousWords: List<String>,
        sensitiveField: Boolean,
        now: Long
    ): CommitOutcome {
        val word = token.trim().lowercase()
        val autocorrectEnabled = flags.autoCorrectionEnabled.first()
        val typoCorrectionEnabled = flags.typoCorrectionEnabled.first()
        val threshold = flags.learningThreshold.first()

        var committed = word
        var correction: CorrectionResult? = null
        if (autocorrectEnabled && typoCorrectionEnabled && !sensitiveField) {
            correction = autoCorrection.correct(word, isBangla, now, correctionEnabled = true)
            if (correction != null) committed = correction.correction
        }

        provider.onWordCommitted(word, isBangla, previousWords, now)

        val nextWords = if (flags.nextWordPredictionEnabled.first()) {
            provider.getNextWordPredictions(listOf(committed), isBangla, 3, now)
        } else emptyList()

        return CommitOutcome(
            committed = committed,
            original = word,
            autoCorrected = correction != null,
            nextWords = nextWords
        )
    }
}
