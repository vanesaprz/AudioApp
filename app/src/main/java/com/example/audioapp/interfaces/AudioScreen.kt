package com.example.audioapp.interfaces

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.audioapp.media.SimpleAudioPlayer
import com.example.audioapp.media.SimpleAudioRecorder
import com.example.audioapp.storage.AppFiles
import java.io.File


@Composable
fun AudioScreen(navController: NavHostController) {
    var context = LocalContext.current
    var currentFile by remember { mutableStateOf<File?>(null) }

    var status by remember { mutableStateOf("Listo para grabar") }
    val (isPermissionGranted, requestPermission) = rememberAudioPermission()

    val player = remember { SimpleAudioPlayer() }
    val recorder = remember { SimpleAudioRecorder() }

    var isRecording by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            recorder.stop()
            player.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("< Volver")
        }
        
        Text("Audio - Grabar y reproducir")
        Text("Permiso microfono: ${if (isPermissionGranted) "OK" else "NO"}")

        Text("Estado: ${status}")

        Button(
            onClick = {
                if (isPermissionGranted) {
                    if (!isRecording) {
                        val newFile = AppFiles.newAudioFile(context)
                        currentFile = newFile

                        recorder.startRecording(context, newFile)
                        status = "Grabando en: ${newFile.name}"
                        isRecording = true
                    } else {
                        recorder.stop()
                        status = "Grabación guardada: ${currentFile?.name}"
                        isRecording = false
                    }
                } else {
                    requestPermission()
                }
            }
        ) {
            Text(if (isRecording) "Detener Grabación" else "Iniciar Grabación")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            currentFile?.let { file -> // Solo entra aquí si no es nulo
                status = "Preparando ${file.name}"
                player.prepareFromFile(
                    file = file,
                    onPrepared = { status = "Audio preparado y listo" },
                    onCompleted = { status = "Reproducción finalizada" },
                    onError = { msg -> status = msg }
                )
            } ?: run {
                status = "Error: Primero debes grabar un audio"
            }
        }) {
            Text("Preparar audio grabado")
        }

        Button(onClick = {
            player.play { status = it }
            if (status == "Preparado") status = "Reproduciendo..."
        }) { Text("Play") }

        Button(onClick = {
            player.pause()
            status = "Pausado"
        }) { Text("Pause") }

        Button(onClick = {
            player.stop()
            status = "Parado"
        }) { Text("Stop") }


    }


}