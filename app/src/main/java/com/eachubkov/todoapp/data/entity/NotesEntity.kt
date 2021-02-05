package com.eachubkov.todoapp.data.entity

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eachubkov.todoapp.utils.Constants.Companion.NOTES_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = NOTES_TABLE)
data class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val image: Bitmap?,
    val colorId: Int
): Parcelable