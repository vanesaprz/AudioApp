package com.example.audioapp.media

import android.media.MediaPlayer
import java.io.File

class SimpleAudioPlayer {
    private var mp: MediaPlayer? = null

    fun prepareFromFile(
        file: File,
        onPrepared: () -> Unit,
        onCompleted: () -> Unit,
        onError: (String) -> Unit
    ) {
        release()
        try {
            if (!file.exists()) {
                onError("El archivo no existe: ${file.name}")
                return
            }

            mp = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                setOnCompletionListener { onCompleted() }
                setOnPreparedListener { onPrepared() }
                prepareAsync()
            }
        } catch (e: Exception) {
            onError("Error al preparar: ${e.message}")
        }
    }

    fun play(onError: (String) -> Unit) {
        try {
            mp?.start() ?: onError("No está preparado")
        } catch (e: Exception) {
            onError("Error play: ${e.message}")
        }
    }

    fun pause() {
        try {
            if (mp?.isPlaying == true) {
                mp?.pause()
            }
        } catch (_: Exception) {
        }

    }

    fun stop() {
        try {
            mp?.stop()
        } catch (e: Exception) {
        } finally {
            release()
        }
    }

    fun release() {
        mp?.release()
        mp = null
    }

    val isPlaying: Boolean
        get() = mp?.isPlaying ?: false
}