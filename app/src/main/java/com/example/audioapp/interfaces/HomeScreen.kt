package com.example.audioapp.interfaces

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.audioapp.model.AudioNote
import com.example.audioapp.storage.AppFiles
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    // Cambiamos File por AudioNote
    var audioFiles by remember { mutableStateOf(listOf<AudioNote>()) }
    var selectedAudio by remember { mutableStateOf<AudioNote?>(null) }
    var status by remember { mutableStateOf("Selecciona un audio") }

    val player = remember { SimpleAudioPlayer() }

    LaunchedEffect(navController.currentBackStackEntry) {
        audioFiles = AppFiles.listAudios(context)
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(16.dp)
    ) {
        Text("Audio Diario", style = MaterialTheme.typography.headlineMedium)

        selectedAudio?.let { audio ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Nota: ${audio.name}", style = MaterialTheme.typography.bodyMedium)
                    Text("Estado: $status", style = MaterialTheme.typography.labelSmall)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            player.play {
                                status = it
                            }
                        }) { Text("Play") }
                        Button(onClick = {
                            player.pause(); status = "Pausado"
                        }) { Text("Pause") }
                        Button(onClick = {
                            player.stop(); status = "Parado"
                        }) { Text("Stop") }
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate(Routes.AUDIO) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Grabar Nueva Nota")
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(audioFiles) { audio ->
                AudioItem(
                    audio = audio,
                    isSelected = selectedAudio?.id == audio.id,
                    onClick = {
                        selectedAudio = audio
                        status = "Preparando..."
                        player.prepareFromFile(
                            file = audio.file,
                            onPrepared = { status = "Listo para reproducir" },
                            onCompleted = { status = "Listo" },
                            onError = { status = it }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun AudioItem(audio: AudioNote, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isSelected) Icons.Default.PlayArrow else Icons.Default.Menu,
                contentDescription = null
            )
            Spacer(Modifier.width(12.dp))
            Column {
                // Ahora usamos las propiedades del objeto AudioNote
                Text(text = audio.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                    ).format(audio.createdAt),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}