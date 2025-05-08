package com.example.notes_app

import androidx.compose.ui.graphics.Color
import java.util.Date

enum class NotePriority(val color: Color) {
    HIGH(Color(0xFFE57373)),      // Red
    URGENT(Color(0xFFFFB74D)),    // Orange
    MEDIUM(Color(0xFFFFF176)),    // Amber
    NORMAL(Color(0xFFE0E0E0))     // Gray
}

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val color: Color,
    val dateTime: Date = Date(),
    val priority: NotePriority = NotePriority.NORMAL
)