package com.example.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class MatchPair(
    val left: String,
    val right: String,
)
