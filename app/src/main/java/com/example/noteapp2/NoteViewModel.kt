package com.example.noteapp2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp2.data.Note
import com.example.noteapp2.data.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel (private val repository: NoteRepository): ViewModel() {
    var notes by mutableStateOf(listOf<Note>())
        private set

    fun loadNotes() {
        viewModelScope.launch {
            notes = repository.getAllNotes()
        }
    }

    fun addNotes(note: Note) {
        viewModelScope.launch {
            repository.insert(note)
            loadNotes()
        }
    }

    fun deleteNotes(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
            loadNotes()
        }
    }

    fun getNoteById(noteId: Int, onNoteFetched: (Note) -> Unit) {
        viewModelScope.launch {
            val note = repository.getNoteById(noteId)
            onNoteFetched(note)
        }
    }
}