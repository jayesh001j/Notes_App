


package com.example.notes_app

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.ui.graphics.Color
import java.util.Date

class NotesDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes_db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NOTES = "notes"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_CONTENT = "content"
        private const val KEY_COLOR = "color"
        private const val KEY_DATE_TIME = "date_time"
        private const val KEY_PRIORITY = "priority"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NOTES (
                $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_TITLE TEXT,
                $KEY_CONTENT TEXT,
                $KEY_COLOR INTEGER,
                $KEY_DATE_TIME INTEGER,
                $KEY_PRIORITY INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Check if columns exist before adding them
            val cursor = db.rawQuery("PRAGMA table_info(notes)", null)
            val columnNames = mutableListOf<String>()

            if (cursor.moveToFirst()) {
                do {
                    val columnNameIndex = cursor.getColumnIndex("name")
                    if (columnNameIndex != -1) {
                        columnNames.add(cursor.getString(columnNameIndex))
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()

            // Add date_time column if it doesn't exist
            if (!columnNames.contains(KEY_DATE_TIME)) {
                db.execSQL("ALTER TABLE $TABLE_NOTES ADD COLUMN $KEY_DATE_TIME INTEGER DEFAULT ${System.currentTimeMillis()}")
            }

            // Add priority column if it doesn't exist
            if (!columnNames.contains(KEY_PRIORITY)) {
                db.execSQL("ALTER TABLE $TABLE_NOTES ADD COLUMN $KEY_PRIORITY INTEGER DEFAULT ${NotePriority.NORMAL.ordinal}")
            }
        }
    }

    fun insertNote(note: Note): Long {
        val db = this.writableDatabase
        try {
            // First, check if the table has all required columns
            val cursor = db.rawQuery("PRAGMA table_info(notes)", null)
            val columnNames = mutableListOf<String>()

            if (cursor.moveToFirst()) {
                do {
                    val columnNameIndex = cursor.getColumnIndex("name")
                    if (columnNameIndex != -1) {
                        columnNames.add(cursor.getString(columnNameIndex))
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()

            // Add missing columns if needed
            if (!columnNames.contains(KEY_DATE_TIME)) {
                db.execSQL("ALTER TABLE $TABLE_NOTES ADD COLUMN $KEY_DATE_TIME INTEGER DEFAULT ${System.currentTimeMillis()}")
            }

            if (!columnNames.contains(KEY_PRIORITY)) {
                db.execSQL("ALTER TABLE $TABLE_NOTES ADD COLUMN $KEY_PRIORITY INTEGER DEFAULT ${NotePriority.NORMAL.ordinal}")
            }

            // Now create ContentValues with all fields
            val values = ContentValues().apply {
                put(KEY_TITLE, note.title)
                put(KEY_CONTENT, note.content)
                put(KEY_COLOR, note.color.value.toLong())
                put(KEY_DATE_TIME, note.dateTime.time)
                put(KEY_PRIORITY, note.priority.ordinal)
            }

            return db.insert(TABLE_NOTES, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        } finally {
            db.close()
        }
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val selectQuery = "SELECT * FROM $TABLE_NOTES ORDER BY $KEY_ID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        try {
            cursor.use {
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID))
                        val title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE))
                        val content = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONTENT))
                        val colorValue = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_COLOR))

                        // Handle new fields with fallback for older database versions
                        val dateTime = try {
                            Date(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DATE_TIME)))
                        } catch (e: Exception) {
                            Date()
                        }

                        val priorityOrdinal = try {
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PRIORITY))
                        } catch (e: Exception) {
                            NotePriority.NORMAL.ordinal
                        }

                        val priority =
                            NotePriority.values().getOrElse(priorityOrdinal) { NotePriority.NORMAL }

                        notes.add(
                            Note(
                                id = id,
                                title = title,
                                content = content,
                                color = Color(colorValue.toULong()),
                                dateTime = dateTime,
                                priority = priority
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return notes
    }

    fun getHighPriorityNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = this.readableDatabase

        try {
            // First check if the priority column exists
            val schemaCheckCursor = db.rawQuery("PRAGMA table_info(notes)", null)
            val columnNames = mutableListOf<String>()

            if (schemaCheckCursor.moveToFirst()) {
                do {
                    val columnNameIndex = schemaCheckCursor.getColumnIndex("name")
                    if (columnNameIndex != -1) {
                        columnNames.add(schemaCheckCursor.getString(columnNameIndex))
                    }
                } while (schemaCheckCursor.moveToNext())
            }
            schemaCheckCursor.close()

            // If priority column doesn't exist, add it
            if (!columnNames.contains(KEY_PRIORITY)) {
                val dbWrite = this.writableDatabase
                dbWrite.execSQL("ALTER TABLE $TABLE_NOTES ADD COLUMN $KEY_PRIORITY INTEGER DEFAULT ${NotePriority.NORMAL.ordinal}")
                dbWrite.close()
                // Return empty list since no high priority notes exist yet
                return notes
            }

            // Now query for high priority notes
            val selectQuery =
                "SELECT * FROM $TABLE_NOTES WHERE $KEY_PRIORITY = ? OR $KEY_PRIORITY = ? ORDER BY $KEY_ID DESC"
            val notesCursor = db.rawQuery(
                selectQuery, arrayOf(
                    NotePriority.HIGH.ordinal.toString(),
                    NotePriority.URGENT.ordinal.toString()
                )
            )

            notesCursor.use {
                if (notesCursor.moveToFirst()) {
                    do {
                        val id = notesCursor.getLong(notesCursor.getColumnIndexOrThrow(KEY_ID))
                        val title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(KEY_TITLE))
                        val content = notesCursor.getString(notesCursor.getColumnIndexOrThrow(KEY_CONTENT))
                        val colorValue = notesCursor.getLong(notesCursor.getColumnIndexOrThrow(KEY_COLOR))

                        // Handle new fields with fallback for older database versions
                        val dateTime = try {
                            Date(notesCursor.getLong(notesCursor.getColumnIndexOrThrow(KEY_DATE_TIME)))
                        } catch (e: Exception) {
                            Date()
                        }

                        val priorityOrdinal = try {
                            notesCursor.getInt(notesCursor.getColumnIndexOrThrow(KEY_PRIORITY))
                        } catch (e: Exception) {
                            NotePriority.NORMAL.ordinal
                        }

                        val priority = NotePriority.values().getOrElse(priorityOrdinal) { NotePriority.NORMAL }

                        notes.add(
                            Note(
                                id = id,
                                title = title,
                                content = content,
                                color = Color(colorValue.toULong()),
                                dateTime = dateTime,
                                priority = priority
                            )
                        )
                    } while (notesCursor.moveToNext())
                }
            }
            db.close()
            return notes
        } catch (e: Exception) {
            e.printStackTrace()
            db.close()
            return notes
        }
    }


    fun updateNote(id: Long, note: Note): Int {
        val db = this.writableDatabase
        try {
            // Check if the table has the required columns
            val cursor = db.rawQuery("PRAGMA table_info(notes)", null)
            val columnNames = mutableListOf<String>()

            if (cursor.moveToFirst()) {
                do {
                    val columnNameIndex = cursor.getColumnIndex("name")
                    if (columnNameIndex != -1) {
                        columnNames.add(cursor.getString(columnNameIndex))
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()

            // Create ContentValues with only existing columns
            val values = ContentValues().apply {
                put(KEY_TITLE, note.title)
                put(KEY_CONTENT, note.content)
                put(KEY_COLOR, note.color.value.toLong())

                // Only add date_time if the column exists
                if (columnNames.contains(KEY_DATE_TIME)) {
                    put(KEY_DATE_TIME, note.dateTime.time)
                }

                // Only add priority if the column exists
                if (columnNames.contains(KEY_PRIORITY)) {
                    put(KEY_PRIORITY, note.priority.ordinal)
                }
            }

            val result = db.update(TABLE_NOTES, values, "$KEY_ID = ?", arrayOf(id.toString()))
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        } finally {
            db.close()
        }
    }

    fun deleteNote(id: Long): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NOTES, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
}
