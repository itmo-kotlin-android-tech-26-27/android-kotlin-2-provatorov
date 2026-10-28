package com.example.quiz.storage

import java.io.File

class StorageException(message: String, val file: File) : Exception(message)
