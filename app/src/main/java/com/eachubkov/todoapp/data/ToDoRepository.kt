package com.eachubkov.todoapp.data

import androidx.lifecycle.LiveData
import com.eachubkov.todoapp.data.entity.NotesEntity

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllNotes: LiveData<List<NotesEntity>> = toDoDao.getAllNotes()

    suspend fun addNote(note: NotesEntity) = toDoDao.addNote(note)

    suspend fun updateNote(note: NotesEntity) = toDoDao.updateNote(note)

    suspend fun deleteNote(note: NotesEntity) = toDoDao.deleteNote(note)

    suspend fun deleteAllNotes() = toDoDao.deleteAllNotes()

    fun searchNotes(searchQuery: String) : LiveData<List<NotesEntity>> = toDoDao.searchNotes(searchQuery)
}