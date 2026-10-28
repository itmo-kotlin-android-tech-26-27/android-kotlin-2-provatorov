package com.example.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class ExportedAttempt(
    val score: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val attempt: Attempt,
)
