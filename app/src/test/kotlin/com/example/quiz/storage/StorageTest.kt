package com.example.quiz.storage

import com.example.quiz.model.Attempt
import com.example.quiz.model.AttemptStatus
import com.example.quiz.model.TestSettings
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class StorageTest {
    @get:Rule
    val folder = TemporaryFolder()

    private val date = LocalDateTime(2026, 10, 27, 12, 30)

    @Test
    fun firstStartCreatesDemoBankAndDefaultSettings() {
        val storage = Storage(folder.root)

        assertTrue(storage.bank.value.topics.isNotEmpty())
        assertEquals(TestSettings(), storage.settings.value)
        assertTrue(File(folder.root, "questions.json").exists())
    }

    @Test
    fun savedDataIsReadAfterRestart() {
        val storage = Storage(folder.root)
        storage.saveSettings(TestSettings(timeLimitSeconds = 60, questionsPerTopic = 3))
        storage.addAttempt(Attempt("a1", "ivan", date, date, AttemptStatus.ABORTED))

        val restarted = Storage(folder.root)

        assertEquals(60, restarted.settings.value.timeLimitSeconds)
        assertEquals("ivan", restarted.attempts.value.single().studentId)
        assertEquals(date, restarted.attempts.value.single().startedAt)
    }

    @Test
    fun deletedAttemptDisappears() {
        val storage = Storage(folder.root)
        storage.addAttempt(Attempt("a1", "ivan", date, date, AttemptStatus.COMPLETED))

        storage.deleteAttempt("a1")

        assertTrue(Storage(folder.root).attempts.value.isEmpty())
    }

    @Test(expected = StorageException::class)
    fun brokenFileGivesStorageException() {
        File(folder.root, "settings.json").writeText("{ это не json")

        Storage(folder.root)
    }
}
