package com.tamdt.mynotes.reponsitory

import androidx.room.Query
import com.tamdt.mynotes.database.NoteDatabase
import com.tamdt.mynotes.model.Note

class NoteRespository (private val db: NoteDatabase){
    suspend fun insertNote(note: Note) = db.getNoteDao().insertNote(note)
    suspend fun deleteNote(note: Note) = db.getNoteDao().deleteNote(note)
    suspend fun updateNote(note: Note) = db.getNoteDao().updateNote(note)

    fun getAllNote() = db.getNoteDao().getAllNotes()
    fun searchNote(query: String?) = db.getNoteDao().searchNote(query)
}