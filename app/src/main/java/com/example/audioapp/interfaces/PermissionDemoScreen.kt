package com.example.audioapp.interfaces

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.audioapp.interfaces.rememberPermissionState
import com.example.audioapp.storage.AppFiles

@Composable
fun PermissionDemoScreen(){
    val context = LocalContext.current
    val (audioGranted, requesteAudio) = rememberAudioPermission()

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Permiso de Audio")
        Text(if (audioGranted) "Concedido" else "No concedido")

        Button(onClick = {
            val file = AppFiles.audioFile(context)
            Toast.makeText(context, "Audio grabado en: \n${file.absolutePath}",Toast.LENGTH_LONG).show()
        },
            enabled= audioGranted
            ) { Text("Graba audio(demo)")}
    }
}


