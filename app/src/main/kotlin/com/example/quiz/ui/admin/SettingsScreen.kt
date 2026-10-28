package com.example.quiz.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quiz.model.TestSettings
import com.example.quiz.storage.Storage
import com.example.quiz.storage.StorageException
import com.example.quiz.ui.AppTopBar
import com.example.quiz.ui.formatSeconds

@Composable
fun SettingsScreen(storage: Storage, onBack: () -> Unit) {
    val current by storage.settings.collectAsStateWithLifecycle()
    var timeText by rememberSaveable { mutableStateOf(current.timeLimitSeconds.toString()) }
    var countText by rememberSaveable { mutableStateOf(current.questionsPerTopic.toString()) }
    var timeError by rememberSaveable { mutableStateOf<String?>(null) }
    var countError by rememberSaveable { mutableStateOf<String?>(null) }
    var message by rememberSaveable { mutableStateOf<String?>(null) }

    fun save() {
        val seconds = timeText.trim().toIntOrNull()
        val count = countText.trim().toIntOrNull()
        timeError = if (seconds != null && seconds > 0) null else "Введите целое число секунд больше нуля"
        countError = if (count != null && count > 0) null else "Введите целое число больше нуля"
        if (seconds == null || seconds <= 0 || count == null || count <= 0) {
            message = null
            return
        }
        try {
            storage.saveSettings(TestSettings(seconds, count))
            message = "Параметры сохранены. Они действуют для следующих попыток."
        } catch (e: StorageException) {
            message = e.message
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = { AppTopBar(title = "Параметры теста", onBack = onBack) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val secondsPreview = timeText.trim().toIntOrNull()
            OutlinedTextField(
                value = timeText,
                onValueChange = {
                    timeText = it
                    timeError = null
                    message = null
                },
                label = { Text("Лимит времени на тест, секунд") },
                singleLine = true,
                isError = timeError != null,
                supportingText = {
                    val error = timeError
                    if (error != null) {
                        Text(error)
                    } else if (secondsPreview != null && secondsPreview > 0) {
                        Text("Это ${formatSeconds(secondsPreview)} (минуты:секунды)")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = countText,
                onValueChange = {
                    countText = it
                    countError = null
                    message = null
                },
                label = { Text("Лимит вопросов на тему") },
                singleLine = true,
                isError = countError != null,
                supportingText = {
                    val error = countError
                    if (error != null) {
                        Text(error)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
            Button(onClick = { save() }, modifier = Modifier.fillMaxWidth()) {
                Text("Сохранить")
            }
            val currentMessage = message
            if (currentMessage != null) {
                Text(currentMessage, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
