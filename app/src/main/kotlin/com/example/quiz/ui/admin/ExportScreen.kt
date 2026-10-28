package com.example.quiz.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.quiz.storage.Storage
import com.example.quiz.storage.saveToDownloads
import com.example.quiz.ui.AppTopBar
import com.example.quiz.ui.currentDateTime
import com.example.quiz.ui.formatForFileName

@Composable
fun ExportScreen(storage: Storage, onBack: () -> Unit) {
    val context = LocalContext.current
    var resultText by rememberSaveable { mutableStateOf<String?>(null) }
    var errorText by rememberSaveable { mutableStateOf<String?>(null) }

    fun export() {
        try {
            val now = currentDateTime()
            val fileName = "quiz-export-${formatForFileName(now)}.json"
            val json = storage.exportJson(now)
            val folder = saveToDownloads(context, fileName, json)
            val sizeKb = (json.toByteArray().size + 1023) / 1024
            resultText = "Имя: $fileName\nПапка: $folder\nРазмер: $sizeKb КБ\n" +
                "Попыток в файле: ${storage.attempts.value.size}"
            errorText = null
        } catch (e: Exception) {
            resultText = null
            errorText = "Не удалось создать файл: ${e.message}"
        }
    }

    Scaffold(topBar = { AppTopBar(title = "Выгрузка", onBack = onBack) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                "В файл попадут банк вопросов, параметры теста и все попытки студентов " +
                    "с разбором каждого ответа. Файл можно открыть и проанализировать без приложения.",
            )
            Button(onClick = { export() }, modifier = Modifier.fillMaxWidth()) {
                Text("Выгрузить в JSON")
            }

            val result = resultText
            if (result != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text("✓ Файл создан", style = MaterialTheme.typography.titleMedium)
                        Text(result)
                    }
                }
            }

            val error = errorText
            if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
