package com.example.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Topic(
    val id: String,
    val title: String,
) {
    init {
        require(title.isNotBlank()) { "Название темы не может быть пустым" }
    }
}
