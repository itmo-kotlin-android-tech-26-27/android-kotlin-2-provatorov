package com.example.quiz.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Attempt(
    val id: String,
    val studentId: String,
    val startedAt: LocalDateTime,
    val finishedAt: LocalDateTime,
    val status: AttemptStatus,
    val topicResults: List<TopicResult> = emptyList(),
) {
    val score: Int
        get() = topicResults.sumOf { it.score }

    val correctCount: Int
        get() = topicResults.sumOf { it.correctCount }

    val wrongCount: Int
        get() = topicResults.sumOf { it.wrongCount }

    val timedOutCount: Int
        get() = topicResults.sumOf { result -> result.answers.count { it.outcome == AnswerOutcome.TIMED_OUT } }
}
