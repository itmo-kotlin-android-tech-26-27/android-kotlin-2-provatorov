package com.example.quiz.engine

import com.example.quiz.model.ChoiceQuestion
import com.example.quiz.model.MatchQuestion
import com.example.quiz.model.OpenQuestion

object AnswerChecker {
    fun checkChoice(question: ChoiceQuestion, chosenIndex: Int): Boolean =
        chosenIndex == question.correctIndex

    fun checkOpen(question: OpenQuestion, answer: String): Boolean =
        normalize(answer) == normalize(question.referenceAnswer)

    fun checkMatch(question: MatchQuestion, chosenRights: List<String>): Boolean =
        chosenRights == question.pairs.map { it.right }

    fun normalize(text: String): String =
        text.lowercase().split(Regex("\\s+")).filter { it.isNotEmpty() }.joinToString(" ")
}
