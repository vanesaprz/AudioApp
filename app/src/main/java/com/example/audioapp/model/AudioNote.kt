package com.example.audioapp.model

import java.io.File
import java.util.Date

data class AudioNote(
    val id: String,
    val name: String,
    val file: File,
    val createdAt: Date
)