package com.example.quiz.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data class QuizRoute(val login: String, val topicId: String)
