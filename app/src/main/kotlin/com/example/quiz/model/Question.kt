package com.example.quiz.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Question {
    abstract val id: String
    abstract val topicId: String
    abstract val level: Int
    abstract val text: String
    abstract val correctAnswerText: String
}
