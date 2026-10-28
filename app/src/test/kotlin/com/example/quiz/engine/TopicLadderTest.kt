package com.example.quiz.engine

import com.example.quiz.model.OpenQuestion
import com.example.quiz.model.Topic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TopicLadderTest {
    private val topic = Topic("t1", "Тема")

    private val questions = listOf(
        OpenQuestion("a1", "t1", 1, "Вопрос 1", "1"),
        OpenQuestion("a2", "t1", 1, "Вопрос 2", "2"),
        OpenQuestion("b1", "t1", 2, "Вопрос 3", "3"),
        OpenQuestion("b2", "t1", 2, "Вопрос 4", "4"),
        OpenQuestion("c1", "t1", 3, "Вопрос 5", "5"),
    )

    @Test
    fun firstQuestionIsOnTheEasiestLevel() {
        val ladder = TopicLadder(topic, questions, questionLimit = 10)

        val question = ladder.nextQuestion()

        assertEquals(1, question?.level)
        assertEquals(1, ladder.step)
        assertEquals(3, ladder.stepCount)
    }

    @Test
    fun correctAnswerRaisesLevelAndWrongAnswerLowersIt() {
        val ladder = TopicLadder(topic, questions, questionLimit = 10)

        ladder.recordAnswer(ladder.nextQuestion()!!, "ответ", isCorrect = true)
        val second = ladder.nextQuestion()!!
        assertEquals(2, second.level)

        ladder.recordAnswer(second, "ответ", isCorrect = false)
        assertEquals(1, ladder.nextQuestion()?.level)
    }

    @Test
    fun wrongAnswerOnFirstLevelKeepsFirstLevel() {
        val ladder = TopicLadder(topic, questions, questionLimit = 10)

        ladder.recordAnswer(ladder.nextQuestion()!!, "ответ", isCorrect = false)

        assertEquals(1, ladder.nextQuestion()?.level)
    }

    @Test
    fun topicEndsAfterCorrectAnswerOnTopLevel() {
        val ladder = TopicLadder(topic, questions, questionLimit = 10)

        repeat(3) {
            ladder.recordAnswer(ladder.nextQuestion()!!, "ответ", isCorrect = true)
        }

        assertTrue(ladder.isFinished)
        assertNull(ladder.nextQuestion())
        assertTrue(ladder.toResult().reachedTopLevel)
    }

    @Test
    fun topicEndsWhenQuestionLimitIsReached() {
        val ladder = TopicLadder(topic, questions, questionLimit = 2)

        ladder.recordAnswer(ladder.nextQuestion()!!, "ответ", isCorrect = false)
        ladder.recordAnswer(ladder.nextQuestion()!!, "ответ", isCorrect = false)

        assertNull(ladder.nextQuestion())
        assertEquals(2, ladder.toResult().answers.size)
    }

    @Test
    fun questionsDoNotRepeat() {
        val ladder = TopicLadder(topic, questions, questionLimit = 10)
        val askedIds = mutableListOf<String>()

        while (true) {
            val question = ladder.nextQuestion() ?: break
            askedIds.add(question.id)
            ladder.recordAnswer(question, "ответ", isCorrect = false)
        }

        assertEquals(questions.size, askedIds.size)
        assertEquals(askedIds.size, askedIds.toSet().size)
    }
}
