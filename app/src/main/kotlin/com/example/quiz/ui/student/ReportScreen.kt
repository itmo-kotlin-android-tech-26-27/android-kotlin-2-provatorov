package com.example.quiz.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quiz.model.AnswerOutcome
import com.example.quiz.model.AnswerRecord
import com.example.quiz.model.Attempt
import com.example.quiz.storage.Storage
import com.example.quiz.ui.AppTopBar
import com.example.quiz.ui.BottomButtons
import com.example.quiz.ui.EmptyMessage
import com.example.quiz.ui.StatusLabel
import com.example.quiz.ui.formatDate

@Composable
fun ReportScreen(
    storage: Storage,
    attemptId: String,
    isAdmin: Boolean,
    onRetry: (login: String, topicId: String) -> Unit,
    onTopicsClick: (login: String) -> Unit,
    onBack: () -> Unit,
) {
    val attempts by storage.attempts.collectAsStateWithLifecycle()
    val attempt = attempts.find { it.id == attemptId }
    val topicId = attempt?.topicResults?.firstOrNull()?.topicId

    Scaffold(
        topBar = { AppTopBar(title = "Отчёт о попытке", onBack = onBack) },
        bottomBar = {
            if (attempt != null && !isAdmin) {
                BottomButtons {
                    OutlinedButton(
                        onClick = { onTopicsClick(attempt.studentId) },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("К темам")
                    }
                    if (topicId != null) {
                        Button(
                            onClick = { onRetry(attempt.studentId, topicId) },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Пройти ещё раз")
                        }
                    }
                }
            }
        },
    ) { padding ->
        if (attempt == null) {
            EmptyMessage(
                text = "Попытка не найдена. Возможно, её удалил администратор.",
                modifier = Modifier.padding(padding),
            )
        } else {
            val answers = attempt.topicResults.flatMap { it.answers }
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item { SummaryCard(attempt) }
                item {
                    Text("Разбор по вопросам", style = MaterialTheme.typography.titleMedium)
                }
                if (answers.isEmpty()) {
                    item {
                        Text(
                            "Ответов нет: тест закончился до первого ответа.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                itemsIndexed(answers) { index, answer ->
                    AnswerCard(number = index + 1, answer = answer)
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(attempt: Attempt) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Итог", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                StatusLabel(attempt.status)
            }
            Text("Студент: ${attempt.studentId}")
            Text("Дата: ${formatDate(attempt.startedAt)}")
            Text("Балл: ${attempt.score}", style = MaterialTheme.typography.titleMedium)
            Text("Верных ответов: ${attempt.correctCount}")
            Text("Неверных ответов: ${attempt.wrongCount}")
            if (attempt.timedOutCount > 0) {
                Text("Без ответа из-за времени: ${attempt.timedOutCount}")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Text("По темам", style = MaterialTheme.typography.titleSmall)
            for (result in attempt.topicResults) {
                val topLevel = if (result.reachedTopLevel) "да" else "нет"
                Text(
                    "${result.topicTitle}: балл ${result.score}, верно ${result.correctCount}, " +
                        "неверно ${result.wrongCount}, верхняя ступень взята: $topLevel",
                )
            }
        }
    }
}

@Composable
private fun AnswerCard(number: Int, answer: AnswerRecord) {
    val studentAnswer = if (answer.outcome == AnswerOutcome.TIMED_OUT) "—" else answer.studentAnswer
    val resultColor = if (answer.outcome == AnswerOutcome.CORRECT) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.error
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text("$number. ${answer.questionText}", style = MaterialTheme.typography.titleSmall)
            Text("Ответ студента: $studentAnswer")
            Text("Правильный ответ: ${answer.correctAnswerText}")
            Text(
                "Результат: ${answer.outcome.label}, баллов: ${answer.pointsEarned}",
                color = resultColor,
            )
        }
    }
}
