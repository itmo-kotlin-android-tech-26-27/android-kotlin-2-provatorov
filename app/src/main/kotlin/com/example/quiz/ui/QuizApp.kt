package com.example.quiz.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.quiz.QuizApplication
import com.example.quiz.storage.StorageException
import com.example.quiz.ui.navigation.AppNavHost
import java.io.File

@Composable
fun QuizApp(app: QuizApplication) {
    var loadError by remember { mutableStateOf(tryOpenStorage(app)) }
    val storage = app.storage
    val error = loadError

    if (storage != null) {
        AppNavHost(storage)
    } else if (error != null) {
        BrokenDataScreen(
            message = error.message ?: "",
            onReset = {
                error.file.renameTo(File(error.file.path + ".broken"))
                loadError = tryOpenStorage(app)
            },
        )
    }
}

private fun tryOpenStorage(app: QuizApplication): StorageException? {
    try {
        app.openStorage()
        return null
    } catch (e: StorageException) {
        return e
    }
}
