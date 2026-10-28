package com.example.quiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("match")
data class MatchQuestion(
    override val id: String,
    override val topicId: String,
    override val level: Int,
    override val text: String,
    val pairs: List<MatchPair>,
) : Question() {
    init {
        require(level >= 1) { "Уровень сложности должен быть не меньше 1, а был $level" }
        require(pairs.size >= 2) { "В вопросе на соответствие должно быть хотя бы две пары" }
        require(pairs.map { it.right }.toSet().size == pairs.size) { "Правые элементы пар не должны повторяться" }
    }

    override val correctAnswerText: String
        get() = pairs.joinToString("; ") { "${it.left} — ${it.right}" }
}
