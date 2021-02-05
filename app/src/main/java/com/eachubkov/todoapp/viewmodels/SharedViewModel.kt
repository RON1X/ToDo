package com.eachubkov.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.eachubkov.todoapp.data.entity.NotesEntity

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)
    fun checkEmptyDatabase(listNotes: List<NotesEntity>) { emptyDatabase.value = listNotes.isEmpty() }

    val noteColorID: MutableLiveData<Int> = MutableLiveData(0)
    fun saveNoteColorId(colorId: Int) { noteColorID.value = colorId }
}