package com.eachubkov.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eachubkov.todoapp.data.entity.NotesEntity

@Dao
interface ToDoDao {
    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<NotesEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: NotesEntity)

    @Update
    suspend fun updateNote(note: NotesEntity)

    @Delete
    suspend fun deleteNote(note: NotesEntity)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM notes_table WHERE title OR description LIKE :searchQuery")
    fun searchNotes(searchQuery: String): LiveData<List<NotesEntity>>
}