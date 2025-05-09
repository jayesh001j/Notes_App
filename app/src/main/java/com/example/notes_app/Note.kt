package com.example.notes_app

import androidx.compose.ui.graphics.Color
import java.util.Date

enum class NotePriority(val color: Color) {
    HIGH(Color(0xFFFD99FF)),      // Red
    URGENT(Color(0xFF91F48F)),    // Orange
    MEDIUM(Color(0xFFFFF599)),    // Amber
    NORMAL(Color(0xFFB69CFF))     // Gray
}

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val color: Color,
    val dateTime: Date = Date(),
    val priority: NotePriority = NotePriority.NORMAL
)