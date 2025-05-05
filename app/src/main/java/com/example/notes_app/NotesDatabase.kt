


package com.example.notes_app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.ui.graphics.Color

class NotesDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes_db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NOTES = "notes"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_CONTENT = "content"
        private const val KEY_COLOR = "color"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NOTES (
                $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_TITLE TEXT,
                $KEY_CONTENT TEXT,
                $KEY_COLOR INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    fun insertNote(note: Note): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TITLE, note.title)
            put(KEY_CONTENT, note.content)
            put(KEY_COLOR, note.color.value.toLong())
        }
        val id = db.insert(TABLE_NOTES, null, values)
        db.close()
        return id
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val selectQuery = "SELECT * FROM $TABLE_NOTES ORDER BY $KEY_ID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE))
                    val content = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONTENT))
                    val colorValue = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_COLOR))
                    notes.add(Note(title, content, Color(colorValue.toULong())))
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return notes
    }

    fun updateNote(id: Long, note: Note): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TITLE, note.title)
            put(KEY_CONTENT, note.content)
            put(KEY_COLOR, note.color.value.toLong())
        }
        val result = db.update(TABLE_NOTES, values, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun deleteNote(id: Long): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NOTES, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
}
