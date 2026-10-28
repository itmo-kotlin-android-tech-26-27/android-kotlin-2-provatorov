package com.example.quiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("choice")
data class ChoiceQuestion(
    override val id: String,
    override val topicId: String,
    override val level: Int,
    override val text: String,
    val options: List<String>,
    val correctIndex: Int,
) : Question() {
    init {
        require(level >= 1) { "Уровень сложности должен быть не меньше 1, а был $level" }
        require(options.size >= 2) { "У тестового вопроса должно быть хотя бы два варианта ответа" }
        require(correctIndex in options.indices) { "Правильный вариант указан за границами списка" }
    }

    override val correctAnswerText: String
        get() = options[correctIndex]
}
