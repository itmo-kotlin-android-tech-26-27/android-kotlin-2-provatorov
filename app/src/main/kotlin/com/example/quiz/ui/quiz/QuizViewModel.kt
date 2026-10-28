package com.example.quiz.ui.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz.engine.AnswerChecker
import com.example.quiz.engine.TopicLadder
import com.example.quiz.model.Attempt
import com.example.quiz.model.AttemptStatus
import com.example.quiz.model.ChoiceQuestion
import com.example.quiz.model.MatchQuestion
import com.example.quiz.model.OpenQuestion
import com.example.quiz.storage.Storage
import com.example.quiz.storage.StorageException
import com.example.quiz.ui.currentDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class QuizViewModel(
    private val storage: Storage,
    private val login: String,
    topicId: String,
) : ViewModel() {
    var uiState by mutableStateOf(QuizUiState())
        private set

    var secondsLeft by mutableIntStateOf(0)
        private set

    val timeLimitSeconds = storage.settings.value.timeLimitSeconds

    private val startedAt = currentDateTime()
    private val deadline = System.currentTimeMillis() + timeLimitSeconds * 1000L
    private val ladder = createLadder(topicId)
    private var isFinished = false

    init {
        if (ladder == null) {
            uiState = QuizUiState(fatalError = "В этой теме нет вопросов. Выберите другую тему.")
        } else {
            showNextQuestion()
            startTimer()
        }
    }

    fun selectOption(index: Int) {
        uiState = uiState.copy(selectedOption = index, errorMessage = null)
    }

    fun changeOpenAnswer(text: String) {
        uiState = uiState.copy(openAnswer = text, errorMessage = null)
    }

    fun selectLeft(index: Int) {
        uiState = uiState.copy(
            matchedRights = uiState.matchedRights - index,
            selectedLeft = index,
            errorMessage = null,
        )
    }

    fun selectRight(index: Int) {
        val question = uiState.question
        val left = uiState.selectedLeft
        if (question !is MatchQuestion || left == null) return

        val pairs = uiState.matchedRights.filterValues { it != index } + (left to index)
        val nextLeft = question.pairs.indices.firstOrNull { it !in pairs }
        uiState = uiState.copy(matchedRights = pairs, selectedLeft = nextLeft, errorMessage = null)
    }

    fun confirmAnswer() {
        val ladder = ladder ?: return
        val question = uiState.question ?: return
        if (isFinished) return
        if (System.currentTimeMillis() >= deadline) {
            timeOut()
            return
        }

        when (question) {
            is ChoiceQuestion -> {
                val index = uiState.selectedOption
                if (index == null) {
                    showError("Выберите ответ.")
                    return
                }
                ladder.recordAnswer(question, question.options[index], AnswerChecker.checkChoice(question, index))
            }

            is OpenQuestion -> {
                val answer = uiState.openAnswer.trim()
                if (answer.isEmpty()) {
                    showError("Введите ответ.")
                    return
                }
                ladder.recordAnswer(question, answer, AnswerChecker.checkOpen(question, answer))
            }

            is MatchQuestion -> {
                val pairs = uiState.matchedRights
                if (pairs.size < question.pairs.size) {
                    showError("Составьте пары для всех элементов слева.")
                    return
                }
                val chosenRights = question.pairs.indices.map { uiState.rightItems[pairs.getValue(it)] }
                val answerText = question.pairs.indices.joinToString("; ") {
                    "${question.pairs[it].left} — ${chosenRights[it]}"
                }
                ladder.recordAnswer(question, answerText, AnswerChecker.checkMatch(question, chosenRights))
            }
        }

        showNextQuestion()
    }

    fun abort() {
        finish(AttemptStatus.ABORTED)
    }

    override fun onCleared() {
        finish(AttemptStatus.ABORTED)
    }

    private fun createLadder(topicId: String): TopicLadder? {
        val bank = storage.bank.value
        val topic = bank.topics.find { it.id == topicId }
        val questions = bank.questionsOf(topicId)
        if (topic == null || questions.isEmpty()) {
            return null
        }
        return TopicLadder(topic, questions, storage.settings.value.questionsPerTopic)
    }

    private fun showNextQuestion() {
        val ladder = ladder ?: return
        val question = ladder.nextQuestion()
        if (question == null) {
            finish(AttemptStatus.COMPLETED)
            return
        }

        var rightItems = emptyList<String>()
        var selectedLeft: Int? = null
        if (question is MatchQuestion) {
            rightItems = question.pairs.map { it.right }.shuffled()
            selectedLeft = 0
        }

        uiState = QuizUiState(
            topicTitle = ladder.topic.title,
            step = ladder.step,
            stepCount = ladder.stepCount,
            questionNumber = ladder.askedCount,
            questionCount = ladder.maxQuestions,
            question = question,
            rightItems = rightItems,
            selectedLeft = selectedLeft,
        )
    }

    private fun startTimer() {
        updateSecondsLeft()
        viewModelScope.launch {
            while (!isFinished) {
                if (System.currentTimeMillis() >= deadline) {
                    timeOut()
                } else {
                    updateSecondsLeft()
                    delay(200)
                }
            }
        }
    }

    private fun updateSecondsLeft() {
        val millisLeft = deadline - System.currentTimeMillis()
        secondsLeft = ((millisLeft + 999) / 1000).toInt().coerceAtLeast(0)
    }

    private fun timeOut() {
        val question = uiState.question
        if (question != null) {
            ladder?.recordTimeOut(question)
        }
        secondsLeft = 0
        finish(AttemptStatus.TIMED_OUT)
    }

    private fun finish(status: AttemptStatus) {
        val ladder = ladder ?: return
        if (isFinished) return
        isFinished = true

        val attempt = Attempt(
            id = UUID.randomUUID().toString(),
            studentId = login,
            startedAt = startedAt,
            finishedAt = currentDateTime(),
            status = status,
            topicResults = listOf(ladder.toResult()),
        )
        try {
            storage.addAttempt(attempt)
            uiState = uiState.copy(finishedAttemptId = attempt.id)
        } catch (e: StorageException) {
            uiState = uiState.copy(fatalError = "Не удалось сохранить попытку. ${e.message}")
        }
    }

    private fun showError(message: String) {
        uiState = uiState.copy(errorMessage = message)
    }
}
