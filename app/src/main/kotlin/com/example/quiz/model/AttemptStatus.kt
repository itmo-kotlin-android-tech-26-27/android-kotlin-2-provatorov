package com.example.quiz.model

import kotlinx.serialization.Serializable

@Serializable
enum class AttemptStatus(val label: String) {
    COMPLETED("завершена"),
    ABORTED("прервана"),
    TIMED_OUT("время вышло"),
}
