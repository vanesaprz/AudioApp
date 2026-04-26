package com.example.audioapp.interfaces

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
            if (isRecording) recorder.stop()
            player.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Nueva Nota de Voz",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SuggestionChip(
                onClick = { if (!isPermissionGranted) requestPermission() },
                label = { Text(if (isPermissionGranted) "Micrófono OK" else "Sin Permiso de Micrófono") },
                icon = {
                    Icon(
                        imageVector = if (isPermissionGranted) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (isPermissionGranted) Color(0xFF4CAF50) else Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(12.dp))
                Text(text = status, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

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
            if (status == "Audio preparado y listo") status = "Reproduciendo..."
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