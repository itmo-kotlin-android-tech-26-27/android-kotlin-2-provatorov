package com.example.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class AnswerRecord(
    val questionId: String,
    val questionText: String,
    val level: Int,
    val studentAnswer: String,
    val correctAnswerText: String,
    val outcome: AnswerOutcome,
    val pointsEarned: Int,
)
