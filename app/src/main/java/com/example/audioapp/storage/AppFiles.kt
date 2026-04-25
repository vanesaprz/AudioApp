package com.example.audioapp.storage

import android.content.Context
import com.example.audioapp.model.AudioNote
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


object AppFiles {
    fun audioFile(context: Context): File =
        File(context.filesDir, "grabacion.m4a")

    fun newAudioFile(context: Context): File {
        val ts = SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(Date())
        return File(context.filesDir, "grabacion_$ts.m4a")
    }

    fun listAudios(context: Context): List<AudioNote> {
        val files = context.filesDir.listFiles { file ->
            file.extension == "m4a"
        } ?: return emptyList()

        return files.map { file ->
            AudioNote(
                id = file.name,
                name = file.name,
                file = file,
                createdAt = Date(file.lastModified())
            )
        }.sortedByDescending { it.createdAt }
    }
}