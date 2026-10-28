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

@Composable
fun BankTopicsScreen(
    storage: Storage,
    onTopicClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    val bank by storage.bank.collectAsStateWithLifecycle()

    Scaffold(topBar = { AppTopBar(title = "Банк вопросов", onBack = onBack) }) { padding ->
        if (bank.topics.isEmpty()) {
            EmptyMessage(text = "В банке пока нет тем.", modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(bank.topics, key = { it.id }) { topic ->
                    val questionCount = bank.questionsOf(topic.id).size
                    Card(onClick = { onTopicClick(topic.id) }, modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(topic.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                if (questionCount == 0) "Вопросов нет" else "Вопросов: $questionCount",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}
