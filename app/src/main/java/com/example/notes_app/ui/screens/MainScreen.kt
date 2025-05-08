package com.example.notes_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.notes_app.NoteTab
import com.example.notes_app.NoteViewModel
import com.example.notes_app.ui.components.ModernNotesBottomNavigation
import com.example.notes_app.ui.components.SettingsScreen
import com.example.notes_app.ui.theme.AppColors

@Composable
fun MainScreen(
    viewModel: NoteViewModel,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val isEditing by viewModel.isEditing.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    
    if (isEditing) {
        NoteEditorScreen(
            viewModel = viewModel,
            onBack = { viewModel.cancelEdit() }
        )
    } else {
        Scaffold(
            backgroundColor = AppColors.getBackgroundColor(isDarkMode),
            bottomBar = {
                ModernNotesBottomNavigation(
                    currentTab = selectedTab,
                    onTabSelected = { tab -> viewModel.selectTab(tab) },
                    isDarkMode = isDarkMode
                )
            },

        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().background(AppColors.getBackgroundColor(isDarkMode))) {
                when (selectedTab) {
                    NoteTab.ALL_NOTES -> {
                        NotesListScreen(
                            viewModel = viewModel,
                            onNoteClick = { viewModel.editNote(it) },
                            onAddNoteClick = { viewModel.startNewNote() },
                            modifier = Modifier.padding(paddingValues),
                            isDarkMode = isDarkMode
                        )
                    }
                    NoteTab.HIGH_PRIORITY -> {
                        NotesListScreen(
                            viewModel = viewModel,
                            onNoteClick = { viewModel.editNote(it) },
                            onAddNoteClick = { viewModel.startNewNote() },
                            modifier = Modifier.padding(paddingValues),
                            isDarkMode = isDarkMode
                        )
                    }
                    NoteTab.SETTINGS -> {
                        SettingsScreen(
                            isDarkMode = isDarkMode,
                            onThemeChange = onThemeChange,
                            modifier = Modifier.padding(paddingValues),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteEditorScreen(
    viewModel: NoteViewModel,
    onBack: () -> Unit
) {
    val currentNote by viewModel.currentNote.collectAsState()
    val isNewNote = currentNote == null
    
    com.example.notes_app.ui.components.NoteEditor(
        title = viewModel.noteTitle.value,
        content = viewModel.noteContent.value,
        priority = viewModel.notePriority.value,
        onTitleChange = { viewModel.noteTitle.value = it },
        onContentChange = { viewModel.noteContent.value = it },
        onPriorityChange = { viewModel.notePriority.value = it },
        onSave = { viewModel.saveNote() },
        onBack = onBack,
        onDelete = { 
            currentNote?.let { viewModel.deleteNote(it.id) }
            onBack()
        },
        isNewNote = isNewNote
    )
}
