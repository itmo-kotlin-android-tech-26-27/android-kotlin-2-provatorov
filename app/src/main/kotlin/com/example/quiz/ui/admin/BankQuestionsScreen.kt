package com.example.quiz.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quiz.storage.Storage
import com.example.quiz.ui.AppTopBar
import com.example.quiz.ui.EmptyMessage
import com.example.quiz.ui.questionTypeLabel

@Composable
fun BankQuestionsScreen(
    storage: Storage,
    topicId: String,
    onQuestionClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    val bank by storage.bank.collectAsStateWithLifecycle()
    val topic = bank.topics.find { it.id == topicId }
    val questions = bank.questionsOf(topicId).sortedBy { it.level }

    Scaffold(topBar = { AppTopBar(title = "Вопросы темы", onBack = onBack) }) { padding ->
        if (topic == null) {
            EmptyMessage(text = "Тема не найдена.", modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Text(topic.title, style = MaterialTheme.typography.titleLarge)
                }
                if (questions.isEmpty()) {
                    item {
                        Text(
                            "В этой теме пока нет вопросов.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                items(questions, key = { it.id }) { question ->
                    Card(onClick = { onQuestionClick(question.id) }, modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                "${questionTypeLabel(question)} · уровень ${question.level}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Text(question.text, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}
