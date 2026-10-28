package com.example.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class TopicResult(
    val topicId: String,
    val topicTitle: String,
    val answers: List<AnswerRecord> = emptyList(),
    val reachedTopLevel: Boolean = false,
) {
    val score: Int
        get() = answers.sumOf { it.pointsEarned }

    val correctCount: Int
        get() = answers.count { it.outcome == AnswerOutcome.CORRECT }

    val wrongCount: Int
        get() = answers.count { it.outcome == AnswerOutcome.WRONG }
}
