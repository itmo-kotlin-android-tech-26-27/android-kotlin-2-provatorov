package com.example.quiz.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data class ReportRoute(val attemptId: String, val isAdmin: Boolean = false)
