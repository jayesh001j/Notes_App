package com.example.notes_app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(private val database: NotesDatabase) {

    suspend fun getAllNotes(): List<Note> = withContext(Dispatchers.IO) {
        database.getAllNotes()
    }

    suspend fun getHighPriorityNotes(): List<Note> = withContext(Dispatchers.IO) {
        database.getHighPriorityNotes()
    }

    suspend fun insertNote(note: Note): Long = withContext(Dispatchers.IO) {
        database.insertNote(note)
    }

    suspend fun updateNote(id: Long, note: Note): Int = withContext(Dispatchers.IO) {
        database.updateNote(id, note)
    }

    suspend fun deleteNote(id: Long): Int = withContext(Dispatchers.IO) {
        database.deleteNote(id)
    }
}
