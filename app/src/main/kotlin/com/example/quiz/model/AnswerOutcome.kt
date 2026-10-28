package com.example.quiz.model

import kotlinx.serialization.Serializable

@Serializable
enum class AnswerOutcome(val label: String) {
    CORRECT("верно"),
    WRONG("неверно"),
    TIMED_OUT("время вышло"),
}
