package com.example.notes_app

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class ThemePreferences(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isDarkMode: Boolean
        get() = preferences.getBoolean(KEY_DARK_MODE, false)
        set(value) {
            preferences.edit().putBoolean(KEY_DARK_MODE, value).apply()
        }

    companion object {
        private const val PREFS_NAME = "notes_app_preferences"
        private const val KEY_DARK_MODE = "dark_mode"
    }
}

@Composable
fun rememberThemePreference(context: Context = LocalContext.current): State<Boolean> {
    val themePreferences = remember { ThemePreferences(context) }
    val systemInDarkTheme = isSystemInDarkTheme()
    val isDarkMode = remember { mutableStateOf(themePreferences.isDarkMode) }
    
    return isDarkMode
}
