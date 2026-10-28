package com.example.quiz.engine

import com.example.quiz.model.AnswerOutcome
import com.example.quiz.model.AnswerRecord
import com.example.quiz.model.Question
import com.example.quiz.model.Topic
import com.example.quiz.model.TopicResult

class TopicLadder(
    val topic: Topic,
    private val questions: List<Question>,
    private val questionLimit: Int,
) {
    private val levels = questions.map { it.level }.distinct().sorted()
    private val answers = mutableListOf<AnswerRecord>()
    private val askedIds = mutableSetOf<String>()
    private var levelIndex = 0
    private var reachedTopLevel = false

    val step: Int
        get() = levelIndex + 1

    val stepCount: Int
        get() = levels.size

    val askedCount: Int
        get() = askedIds.size

    val maxQuestions: Int
        get() = minOf(questionLimit, questions.size)

    val isFinished: Boolean
        get() = reachedTopLevel || askedIds.size >= maxQuestions

    fun nextQuestion(): Question? {
        if (isFinished) {
            return null
        }
        val index = nearestLevelWithFreeQuestions() ?: return null
        levelIndex = index
        val question = freeQuestionsOf(levels[index]).random()
        askedIds.add(question.id)
        return question
    }

    fun recordAnswer(question: Question, studentAnswer: String, isCorrect: Boolean) {
        if (isCorrect) {
            answers.add(record(question, studentAnswer, AnswerOutcome.CORRECT, question.level))
            if (levelIndex == levels.lastIndex) {
                reachedTopLevel = true
            } else {
                levelIndex++
            }
        } else {
            answers.add(record(question, studentAnswer, AnswerOutcome.WRONG, 0))
            if (levelIndex > 0) {
                levelIndex--
            }
        }
    }

    fun recordTimeOut(question: Question) {
        answers.add(record(question, "", AnswerOutcome.TIMED_OUT, 0))
    }

    fun toResult(): TopicResult = TopicResult(topic.id, topic.title, answers.toList(), reachedTopLevel)

    private fun nearestLevelWithFreeQuestions(): Int? {
        for (distance in levels.indices) {
            val higher = levelIndex + distance
            if (higher in levels.indices && freeQuestionsOf(levels[higher]).isNotEmpty()) {
                return higher
            }
            val lower = levelIndex - distance
            if (lower in levels.indices && freeQuestionsOf(levels[lower]).isNotEmpty()) {
                return lower
            }
        }
        return null
    }

    private fun freeQuestionsOf(level: Int): List<Question> =
        questions.filter { it.level == level && it.id !in askedIds }

    private fun record(question: Question, studentAnswer: String, outcome: AnswerOutcome, points: Int) =
        AnswerRecord(
            questionId = question.id,
            questionText = question.text,
            level = question.level,
            studentAnswer = studentAnswer,
            correctAnswerText = question.correctAnswerText,
            outcome = outcome,
            pointsEarned = points,
        )
}
