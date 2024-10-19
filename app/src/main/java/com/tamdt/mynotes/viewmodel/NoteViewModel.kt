package com.tamdt.mynotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tamdt.mynotes.model.Note
import com.tamdt.mynotes.reponsitory.NoteRespository
import kotlinx.coroutines.launch

class NoteViewModel (app: Application, private val noteRespository: NoteRespository): AndroidViewModel(app) {
    fun addNote(note: Note) =
        viewModelScope.launch {
            noteRespository.insertNote(note)
        }
    fun deleteNote(note: Note) =
        viewModelScope.launch {
            noteRespository.deleteNote(note)
        }
    fun updateNote(note: Note) =
        viewModelScope.launch {
            noteRespository.updateNote(note)
        }
    fun getAllNotes() = noteRespository.getAllNote()
    fun searchNote(query: String?) = noteRespository.searchNote(query)
}