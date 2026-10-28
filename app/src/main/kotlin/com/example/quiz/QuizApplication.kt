package com.example.quiz

import android.app.Application
import com.example.quiz.storage.Storage

class QuizApplication : Application() {
    var storage: Storage? = null
        private set

    fun openStorage() {
        if (storage == null) {
            storage = Storage(filesDir)
        }
    }
}
