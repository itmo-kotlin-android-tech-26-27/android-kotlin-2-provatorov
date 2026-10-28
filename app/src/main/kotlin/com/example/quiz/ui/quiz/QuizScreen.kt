package com.example.quiz.ui.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quiz.model.ChoiceQuestion
import com.example.quiz.model.MatchQuestion
import com.example.quiz.model.OpenQuestion
import com.example.quiz.ui.AppTopBar
import com.example.quiz.ui.BottomButtons
import com.example.quiz.ui.EmptyMessage
import com.example.quiz.ui.formatSeconds
import com.example.quiz.ui.questionTypeLabel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onFinished: (String) -> Unit,
    onBack: () -> Unit,
) {
    val state = viewModel.uiState
    val finishedAttemptId = state.finishedAttemptId
    val fatalError = state.fatalError
    var showAbortDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(finishedAttemptId) {
        if (finishedAttemptId != null) {
            onFinished(finishedAttemptId)
        }
    }

    BackHandler(enabled = fatalError == null && finishedAttemptId == null) {
        showAbortDialog = true
    }

    if (fatalError != null) {
        Scaffold(topBar = { AppTopBar(title = "Тест", onBack = onBack) }) { padding ->
            EmptyMessage(text = fatalError, modifier = Modifier.padding(padding))
        }
        return
    }

    val question = state.question ?: return

    if (showAbortDialog) {
        AlertDialog(
            onDismissRequest = { showAbortDialog = false },
            title = { Text("Прервать тест?") },
            text = { Text("Попытка сохранится со статусом «прервана», уже данные ответы будут засчитаны.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAbortDialog = false
                        viewModel.abort()
                    },
                ) {
                    Text("Прервать")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAbortDialog = false }) {
                    Text("Продолжить")
                }
            },
        )
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            QuizHeader(
                state = state,
                secondsLeft = viewModel.secondsLeft,
                timeLimitSeconds = viewModel.timeLimitSeconds,
            )
        },
        bottomBar = {
            BottomButtons {
                OutlinedButton(onClick = { showAbortDialog = true }, modifier = Modifier.weight(1f)) {
                    Text("Прервать")
                }
                Button(onClick = { viewModel.confirmAnswer() }, modifier = Modifier.weight(1f)) {
                    Text("Ответить")
                }
            }
        },
    ) { padding ->
        key(question.id) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    questionTypeLabel(question),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(question.text, style = MaterialTheme.typography.titleLarge)

                when (question) {
                    is ChoiceQuestion -> ChoiceAnswer(
                        options = question.options,
                        selectedOption = state.selectedOption,
                        onSelect = { viewModel.selectOption(it) },
                    )

                    is OpenQuestion -> OpenAnswer(
                        answer = state.openAnswer,
                        onChange = { viewModel.changeOpenAnswer(it) },
                    )

                    is MatchQuestion -> MatchAnswer(
                        leftItems = question.pairs.map { it.left },
                        rightItems = state.rightItems,
                        matchedRights = state.matchedRights,
                        selectedLeft = state.selectedLeft,
                        onLeftClick = { viewModel.selectLeft(it) },
                        onRightClick = { viewModel.selectRight(it) },
                    )
                }

                val errorMessage = state.errorMessage
                if (errorMessage != null) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun QuizHeader(state: QuizUiState, secondsLeft: Int, timeLimitSeconds: Int) {
    val timeColor = if (secondsLeft <= 30) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(tonalElevation = 3.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(state.topicTitle, style = MaterialTheme.typography.titleLarge)
            Row {
                Text(
                    "Ступень ${state.step} из ${state.stepCount}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    "Осталось ${formatSeconds(secondsLeft)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = timeColor,
                )
            }
            Text(
                "Вопрос ${state.questionNumber} из ${state.questionCount}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            LinearProgressIndicator(
                progress = { secondsLeft.toFloat() / timeLimitSeconds },
                modifier = Modifier.fillMaxWidth(),
                drawStopIndicator = {},
            )
        }
    }
}
