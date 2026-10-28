package com.example.quiz.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ExportFile(
    val exportedAt: LocalDateTime,
    val settings: TestSettings,
    val questionBank: QuestionBank,
    val attempts: List<ExportedAttempt>,
)
