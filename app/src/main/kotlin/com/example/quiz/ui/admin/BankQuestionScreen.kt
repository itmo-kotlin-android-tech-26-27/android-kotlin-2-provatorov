package com.example.quiz.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quiz.model.ChoiceQuestion
import com.example.quiz.model.MatchQuestion
import com.example.quiz.model.OpenQuestion
import com.example.quiz.storage.Storage
import com.example.quiz.ui.AppTopBar
import com.example.quiz.ui.EmptyMessage
import com.example.quiz.ui.questionTypeLabel

@Composable
fun BankQuestionScreen(
    storage: Storage,
    questionId: String,
    onBack: () -> Unit,
) {
    val bank by storage.bank.collectAsStateWithLifecycle()
    val question = bank.questions.find { it.id == questionId }

    Scaffold(topBar = { AppTopBar(title = "Вопрос", onBack = onBack) }) { padding ->
        if (question == null) {
            EmptyMessage(text = "Вопрос не найден.", modifier = Modifier.padding(padding))
            return@Scaffold
        }

        val topicTitle = bank.topics.find { it.id == question.topicId }?.title ?: question.topicId
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Тема: $topicTitle")
            Text("Тип: ${questionTypeLabel(question)}")
            Text("Уровень сложности: ${question.level}")

            SectionTitle("Формулировка")
            Text(question.text, style = MaterialTheme.typography.bodyLarge)

            when (question) {
                is ChoiceQuestion -> {
                    SectionTitle("Варианты ответа")
                    question.options.forEachIndexed { index, option ->
                        if (index == question.correctIndex) {
                            Text("✓ $option — правильный", fontWeight = FontWeight.Bold)
                        } else {
                            Text("• $option")
                        }
                    }
                }

                is OpenQuestion -> {
                    SectionTitle("Эталонный ответ")
                    Text(question.referenceAnswer)
                }

                is MatchQuestion -> {
                    SectionTitle("Правильные пары")
                    for (pair in question.pairs) {
                        Text("${pair.left} — ${pair.right}")
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 8.dp),
    )
}
