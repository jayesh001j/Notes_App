package com.example.notes_app

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    // UI State
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _selectedTab = MutableStateFlow(NoteTab.ALL_NOTES)
    val selectedTab: StateFlow<NoteTab> = _selectedTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote.asStateFlow()

    // Note editor state
    val noteTitle = mutableStateOf("")
    val noteContent = mutableStateOf("")
    val notePriority = mutableStateOf(NotePriority.NORMAL)
    val noteId = mutableStateOf<Long?>(null)

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _notes.value = when (_selectedTab.value) {
                NoteTab.ALL_NOTES -> repository.getAllNotes()
                NoteTab.HIGH_PRIORITY -> repository.getHighPriorityNotes()
                NoteTab.SETTINGS -> emptyList()
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun selectTab(tab: NoteTab) {
        _selectedTab.value = tab
        loadNotes()
    }

    fun startNewNote() {
        _isEditing.value = true
        _currentNote.value = null
        noteTitle.value = ""
        noteContent.value = ""
        notePriority.value = NotePriority.NORMAL
        noteId.value = null
    }

    fun editNote(note: Note) {
        _isEditing.value = true
        _currentNote.value = note
        noteTitle.value = note.title
        noteContent.value = note.content
        notePriority.value = note.priority
        noteId.value = note.id
    }

    fun cancelEdit() {
        _isEditing.value = false
        _currentNote.value = null
    }

    fun saveNote() {
        if (noteTitle.value.isBlank() && noteContent.value.isBlank()) {
            return
        }

        val title = noteTitle.value.ifBlank { "Untitled" }
        val content = noteContent.value
        val priority = notePriority.value
        val color = priority.color

        viewModelScope.launch {
            val note = Note(
                id = noteId.value ?: 0,
                title = title,
                content = content,
                color = color,
                dateTime = Date(),
                priority = priority
            )

            if (noteId.value == null) {
                repository.insertNote(note)
            } else {
                repository.updateNote(noteId.value!!, note)
            }

            _isEditing.value = false
            loadNotes()
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
            loadNotes()
        }
    }

    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    class Factory(private val repository: NoteRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NoteViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

enum class NoteTab {
    ALL_NOTES, HIGH_PRIORITY, SETTINGS
}
