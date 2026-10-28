package com.example.quiz.engine

import com.example.quiz.model.MatchPair
import com.example.quiz.model.MatchQuestion
import com.example.quiz.model.OpenQuestion
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AnswerCheckerTest {
    private val openQuestion = OpenQuestion("q1", "t1", 1, "Столица Франции?", "Париж")

    private val matchQuestion = MatchQuestion(
        id = "q2",
        topicId = "t1",
        level = 1,
        text = "Страна и столица",
        pairs = listOf(MatchPair("Япония", "Токио"), MatchPair("Канада", "Оттава")),
    )

    @Test
    fun openAnswerIgnoresCaseAndExtraSpaces() {
        assertTrue(AnswerChecker.checkOpen(openQuestion, "  пАРИЖ "))
    }

    @Test
    fun openAnswerWithOtherTextIsWrong() {
        assertFalse(AnswerChecker.checkOpen(openQuestion, "Лион"))
    }

    @Test
    fun matchIsCorrectOnlyWhenAllPairsAreRight() {
        assertTrue(AnswerChecker.checkMatch(matchQuestion, listOf("Токио", "Оттава")))
        assertFalse(AnswerChecker.checkMatch(matchQuestion, listOf("Оттава", "Токио")))
    }
}
