package com.example.noteapp2.data

class NoteRepository(private val noteDao: NoteDao) {
    suspend fun insert(note: Note) = noteDao.insert(note)
    suspend fun getAllNotes() = noteDao.getAllNotes()
    suspend fun delete(note: Note) = noteDao.delete(note)
}