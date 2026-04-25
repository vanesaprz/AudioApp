package com.example.audioapp.media

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class SimpleAudioRecorder {
    private var recorder: MediaRecorder? = null

    fun startRecording(context: Context, file: File) {
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }
    }

    fun stop() {
        try {
            recorder?.stop()
        } catch (e: Exception) {
        } finally {
            recorder?.release()
            recorder = null
        }
    }
}