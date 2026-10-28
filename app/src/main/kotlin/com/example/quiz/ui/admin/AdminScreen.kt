package com.example.quiz.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quiz.ui.AppTopBar

@Composable
fun AdminScreen(
    onBankClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAttemptsClick: () -> Unit,
    onExportClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(topBar = { AppTopBar(title = "Администратор", onBack = onBack) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MenuCard("Банк вопросов", "Темы, вопросы и правильные ответы", onBankClick)
            MenuCard("Параметры теста", "Лимит времени и лимит вопросов на тему", onSettingsClick)
            MenuCard("Попытки студентов", "Все попытки, их отчёты и удаление", onAttemptsClick)
            MenuCard("Выгрузка", "Все данные одним JSON-файлом", onExportClick)
        }
    }
}

@Composable
private fun MenuCard(title: String, description: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
