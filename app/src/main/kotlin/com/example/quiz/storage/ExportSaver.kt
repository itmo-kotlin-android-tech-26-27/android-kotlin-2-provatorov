package com.example.quiz.storage

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.IOException

fun saveToDownloads(context: Context, fileName: String, text: String): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values = ContentValues()
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        values.put(MediaStore.Downloads.MIME_TYPE, "application/json")
        values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            ?: throw IOException("система не дала создать файл")
        val stream = resolver.openOutputStream(uri) ?: throw IOException("не удалось открыть файл для записи")
        stream.use { it.write(text.toByteArray()) }
        return "Загрузки (Download)"
    }

    val folder = context.getExternalFilesDir(null) ?: context.filesDir
    File(folder, fileName).writeText(text)
    return folder.path
}
