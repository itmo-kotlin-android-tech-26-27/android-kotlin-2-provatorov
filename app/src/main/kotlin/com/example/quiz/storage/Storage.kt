package com.example.quiz.storage

import com.example.quiz.model.Attempt
import com.example.quiz.model.ExportFile
import com.example.quiz.model.ExportedAttempt
import com.example.quiz.model.QuestionBank
import com.example.quiz.model.TestSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

class Storage(dataDir: File) {
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    private val questionsFile = File(dataDir, "questions.json")
    private val settingsFile = File(dataDir, "settings.json")
    private val attemptsFile = File(dataDir, "attempts.json")

    private val bankState = MutableStateFlow(read(questionsFile) { DemoBank.create() })
    private val settingsState = MutableStateFlow(read(settingsFile) { TestSettings() })
    private val attemptsState = MutableStateFlow(read<List<Attempt>>(attemptsFile) { emptyList() })

    val bank: StateFlow<QuestionBank> = bankState
    val settings: StateFlow<TestSettings> = settingsState
    val attempts: StateFlow<List<Attempt>> = attemptsState

    fun saveSettings(newSettings: TestSettings) {
        write(settingsFile, newSettings)
        settingsState.value = newSettings
    }

    fun addAttempt(attempt: Attempt) {
        val updated = attemptsState.value + attempt
        write(attemptsFile, updated)
        attemptsState.value = updated
    }

    fun deleteAttempt(attemptId: String) {
        val updated = attemptsState.value.filter { it.id != attemptId }
        write(attemptsFile, updated)
        attemptsState.value = updated
    }

    fun exportJson(exportedAt: LocalDateTime): String {
        val exportedAttempts = attemptsState.value.map { ExportedAttempt(it.score, it.correctCount, it.wrongCount, it) }
        val export = ExportFile(exportedAt, settingsState.value, bankState.value, exportedAttempts)
        return json.encodeToString(export)
    }

    private inline fun <reified T> read(file: File, default: () -> T): T {
        if (!file.exists()) {
            val value = default()
            write(file, value)
            return value
        }
        try {
            return json.decodeFromString<T>(file.readText())
        } catch (e: IOException) {
            throw StorageException("Не удалось прочитать файл ${file.name}.", file)
        } catch (e: IllegalArgumentException) {
            throw StorageException("Файл ${file.name} повреждён или имеет неверный формат.", file)
        }
    }

    private inline fun <reified T> write(file: File, value: T) {
        val tempFile = File(file.path + ".tmp")
        try {
            tempFile.writeText(json.encodeToString(value))
            if (!tempFile.renameTo(file)) {
                throw IOException("не удалось заменить файл")
            }
        } catch (e: IOException) {
            throw StorageException("Не удалось сохранить файл ${file.name}.", file)
        }
    }
}
