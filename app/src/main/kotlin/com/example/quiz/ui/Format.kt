package com.example.quiz.ui

import com.example.quiz.model.Attempt
import com.example.quiz.model.ChoiceQuestion
import com.example.quiz.model.MatchQuestion
import com.example.quiz.model.OpenQuestion
import com.example.quiz.model.Question
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
private val fileNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")

fun currentDateTime(): LocalDateTime = java.time.LocalDateTime.now().toKotlinLocalDateTime()

fun formatDate(dateTime: LocalDateTime): String = dateTime.toJavaLocalDateTime().format(dateFormatter)

fun formatForFileName(dateTime: LocalDateTime): String = dateTime.toJavaLocalDateTime().format(fileNameFormatter)

fun formatSeconds(totalSeconds: Int): String = "%d:%02d".format(totalSeconds / 60, totalSeconds % 60)

fun topicsOf(attempt: Attempt): String = attempt.topicResults.joinToString(", ") { it.topicTitle }

fun questionTypeLabel(question: Question): String = when (question) {
    is ChoiceQuestion -> "Тестовый вопрос"
    is OpenQuestion -> "Открытый вопрос"
    is MatchQuestion -> "Вопрос на соответствие"
}
