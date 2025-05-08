package com.example.notes_app.ui.theme

import androidx.compose.ui.graphics.Color

object AppColors {
    // Dark theme colors
    val darkBackground = Color(0xFF232323)
    val darkSurface = Color(0xFF333333)
    val darkPrimary = Color(0xFF6200EE)
    val darkSecondary = Color(0xFF03DAC6)
    val darkText = Color.White
    val darkTextSecondary = Color.White.copy(alpha = 0.6f)
    
    // Light theme colors
    val lightBackground = Color(0xFFF5F5F5)
    val lightSurface = Color(0xFFFFFFFF)
    val lightPrimary = Color(0xFF6200EE)
    val lightSecondary = Color(0xFF03DAC6)
    val lightText = Color(0xFF212121)
    val lightTextSecondary = Color(0xFF757575)
    
    // Function to get colors based on isDarkMode
    fun getBackgroundColor(isDarkMode: Boolean) = if (isDarkMode) darkBackground else lightBackground
    fun getSurfaceColor(isDarkMode: Boolean) = if (isDarkMode) darkSurface else lightSurface
    fun getTextColor(isDarkMode: Boolean) = if (isDarkMode) darkText else lightText
    fun getTextSecondaryColor(isDarkMode: Boolean) = if (isDarkMode) darkTextSecondary else lightTextSecondary
    fun getPrimaryColor(isDarkMode: Boolean) = if (isDarkMode) darkPrimary else lightPrimary
}
