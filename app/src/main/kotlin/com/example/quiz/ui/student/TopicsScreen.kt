package com.example.quiz.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quiz.R
import com.example.quiz.storage.Storage
import com.example.quiz.ui.AppTopBar
import com.example.quiz.ui.EmptyMessage

@Composable
fun TopicsScreen(
    storage: Storage,
    login: String,
    onTopicClick: (String) -> Unit,
    onHistoryClick: () -> Unit,
    onBack: () -> Unit,
) {
    val bank by storage.bank.collectAsStateWithLifecycle()
    val attempts by storage.attempts.collectAsStateWithLifecycle()
    val topics = bank.playableTopics()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Темы",
                onBack = onBack,
                actions = {
                    IconButton(onClick = onHistoryClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_history),
                            contentDescription = "История моих попыток",
                        )
                    }
                },
            )
        },
    ) { padding ->
        if (topics.isEmpty()) {
            EmptyMessage(
                text = "Пока нет тем с вопросами.\nОбратитесь к администратору.",
                modifier = Modifier.padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Text(
                        "Вы вошли как $login. Нажмите на тему, чтобы начать тест.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                items(topics, key = { it.id }) { topic ->
                    val questionCount = bank.questionsOf(topic.id).size
                    val attemptCount = attempts.count { attempt ->
                        attempt.studentId == login && attempt.topicResults.any { it.topicId == topic.id }
                    }
                    Card(onClick = { onTopicClick(topic.id) }, modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(topic.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Вопросов: $questionCount · Ваших попыток: $attemptCount",
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
