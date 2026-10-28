package com.example.quiz.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quiz.storage.Storage
import com.example.quiz.ui.AppTopBar
import com.example.quiz.ui.AttemptCard
import com.example.quiz.ui.EmptyMessage

@Composable
fun HistoryScreen(
    storage: Storage,
    login: String,
    onAttemptClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    val attempts by storage.attempts.collectAsStateWithLifecycle()
    val myAttempts = attempts.filter { it.studentId == login }.sortedByDescending { it.startedAt }

    Scaffold(topBar = { AppTopBar(title = "Мои попытки", onBack = onBack) }) { padding ->
        if (myAttempts.isEmpty()) {
            EmptyMessage(
                text = "У вас пока нет попыток.\nВыберите тему и пройдите тест.",
                modifier = Modifier.padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(myAttempts, key = { it.id }) { attempt ->
                    AttemptCard(
                        attempt = attempt,
                        showLogin = false,
                        onClick = { onAttemptClick(attempt.id) },
                    )
                }
            }
        }
    }
}
