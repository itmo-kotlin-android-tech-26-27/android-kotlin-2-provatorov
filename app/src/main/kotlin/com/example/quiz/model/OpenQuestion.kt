package com.example.quiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("open")
data class OpenQuestion(
    override val id: String,
    override val topicId: String,
    override val level: Int,
    override val text: String,
    val referenceAnswer: String,
) : Question() {
    init {
        require(level >= 1) { "Уровень сложности должен быть не меньше 1, а был $level" }
        require(referenceAnswer.isNotBlank()) { "Эталонный ответ не может быть пустым" }
    }

    override val correctAnswerText: String
        get() = referenceAnswer
}
