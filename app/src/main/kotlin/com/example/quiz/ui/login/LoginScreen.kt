package com.example.quiz.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onStudentLogin: (String) -> Unit, onAdminLogin: () -> Unit) {
    var isStudent by rememberSaveable { mutableStateOf(true) }
    var login by rememberSaveable { mutableStateOf("") }
    var showError by rememberSaveable { mutableStateOf(false) }

    fun signIn() {
        if (!isStudent) {
            onAdminLogin()
        } else if (login.isBlank()) {
            showError = true
        } else {
            onStudentLogin(login.trim())
        }
    }

    Scaffold(modifier = Modifier.imePadding()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Тест-экзаменатор", style = MaterialTheme.typography.headlineMedium)
            Text("Выберите роль и войдите", style = MaterialTheme.typography.bodyLarge)

            Column(modifier = Modifier.selectableGroup()) {
                RoleOption(text = "Студент", selected = isStudent, onClick = { isStudent = true })
                RoleOption(
                    text = "Администратор",
                    selected = !isStudent,
                    onClick = {
                        isStudent = false
                        showError = false
                    },
                )
            }

            if (isStudent) {
                OutlinedTextField(
                    value = login,
                    onValueChange = {
                        login = it
                        showError = false
                    },
                    label = { Text("Логин") },
                    singleLine = true,
                    isError = showError,
                    supportingText = {
                        if (showError) {
                            Text("Введите логин, чтобы войти")
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { signIn() }),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Button(onClick = { signIn() }, modifier = Modifier.fillMaxWidth()) {
                Text("Войти")
            }
        }
    }
}

@Composable
private fun RoleOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}
