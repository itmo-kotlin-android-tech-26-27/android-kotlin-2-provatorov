package com.example.quiz.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quiz.storage.Storage
import com.example.quiz.storage.StorageException
import com.example.quiz.ui.AppTopBar
import com.example.quiz.ui.AttemptCard
import com.example.quiz.ui.EmptyMessage
import com.example.quiz.ui.formatDate

@Composable
fun AttemptsScreen(
    storage: Storage,
    onAttemptClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    val attempts by storage.attempts.collectAsStateWithLifecycle()
    val sortedAttempts = attempts.sortedByDescending { it.startedAt }
    var attemptIdToDelete by rememberSaveable { mutableStateOf<String?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val attemptToDelete = attempts.find { it.id == attemptIdToDelete }
    if (attemptToDelete != null) {
        AlertDialog(
            onDismissRequest = { attemptIdToDelete = null },
            title = { Text("Удалить попытку?") },
            text = {
                Text(
                    "Попытка студента ${attemptToDelete.studentId} от ${formatDate(attemptToDelete.startedAt)} " +
                        "будет удалена без возможности восстановления.",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        try {
                            storage.deleteAttempt(attemptToDelete.id)
                            errorMessage = null
                        } catch (e: StorageException) {
                            errorMessage = e.message
                        }
                        attemptIdToDelete = null
                    },
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { attemptIdToDelete = null }) {
                    Text("Отмена")
                }
            },
        )
    }

    Scaffold(topBar = { AppTopBar(title = "Попытки студентов", onBack = onBack) }) { padding ->
        if (sortedAttempts.isEmpty()) {
            EmptyMessage(
                text = "Студенты ещё не проходили тесты.",
                modifier = Modifier.padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                val error = errorMessage
                if (error != null) {
                    item {
                        Text(error, color = MaterialTheme.colorScheme.error)
                    }
                }
                items(sortedAttempts, key = { it.id }) { attempt ->
                    AttemptCard(
                        attempt = attempt,
                        showLogin = true,
                        onClick = { onAttemptClick(attempt.id) },
                        onDelete = { attemptIdToDelete = attempt.id },
                    )
                }
            }
        }
    }
}
