package com.example.quiz.ui.quiz

import com.example.quiz.model.Question

data class QuizUiState(
    val topicTitle: String = "",
    val step: Int = 0,
    val stepCount: Int = 0,
    val questionNumber: Int = 0,
    val questionCount: Int = 0,
    val question: Question? = null,
    val selectedOption: Int? = null,
    val openAnswer: String = "",
    val rightItems: List<String> = emptyList(),
    val matchedRights: Map<Int, Int> = emptyMap(),
    val selectedLeft: Int? = null,
    val errorMessage: String? = null,
    val fatalError: String? = null,
    val finishedAttemptId: String? = null,
)
