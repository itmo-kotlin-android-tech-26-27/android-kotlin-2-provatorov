package com.example.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class TestSettings(
    val timeLimitSeconds: Int = 300,
    val questionsPerTopic: Int = 7,
) {
    init {
        require(timeLimitSeconds > 0) { "Лимит времени должен быть больше нуля" }
        require(questionsPerTopic > 0) { "Лимит вопросов на тему должен быть больше нуля" }
    }
}
