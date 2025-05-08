package com.example.notes_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.ui.screens.MainScreen
import com.example.notes_app.ui.theme.NotesAppTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val themePreference = remember { ThemePreferences(context) }
            val isDarkMode = remember { mutableStateOf(themePreference.isDarkMode) }
            
            NotesAppTheme(darkTheme = isDarkMode.value) {
                val database = remember { NotesDatabase(context) }
                val repository = remember { NoteRepository(database) }
                val viewModelFactory = remember { NoteViewModel.Factory(repository) }
                val viewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]
                
                MainScreen(
                    viewModel = viewModel,
                    isDarkMode = isDarkMode.value,
                    onThemeChange = { newValue ->
                        isDarkMode.value = newValue
                        themePreference.isDarkMode = newValue
                    }
                )
            }
        }
    }
}