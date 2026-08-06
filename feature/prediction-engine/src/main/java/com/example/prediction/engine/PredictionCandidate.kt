package com.example.prediction.engine

enum class CandidateSource { BUILTIN, PERSONAL, LEARNING, EMOJI, NEXT_WORD }

data class PredictionCandidate(
    val word: String,
    val score: Double,
    val source: CandidateSource,
    val confident: Boolean = true
)

data class CorrectionResult(
    val original: String,
    val correction: String,
    val distance: Double,
    val source: CandidateSource
)
