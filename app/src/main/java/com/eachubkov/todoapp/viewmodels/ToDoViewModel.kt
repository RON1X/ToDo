package com.eachubkov.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.eachubkov.todoapp.data.ToDoDatabase
import com.eachubkov.todoapp.data.ToDoRepository
import com.eachubkov.todoapp.data.entity.NotesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private val repository: ToDoRepository = ToDoRepository(toDoDao)

    val getAllNotes: LiveData<List<NotesEntity>> = repository.getAllNotes

    fun addNote(note: NotesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(note)
        }

    fun updateNote(note: NotesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }

    fun deleteNote(note: NotesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }

    fun deleteAllNotes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotes()
        }

    fun searchNotes(searchQuery: String): LiveData<List<NotesEntity>> {
        return repository.searchNotes(searchQuery)
    }
}