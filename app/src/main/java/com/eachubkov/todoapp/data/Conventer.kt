package com.eachubkov.todoapp.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converter {
    @TypeConverter
    fun fromBitmap(image: Bitmap?): ByteArray? {
        return image?.let {
            val outputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.toByteArray()
        }
    }

    @TypeConverter
    fun toBitmap(image: ByteArray?): Bitmap? {
        return image?.let {
            BitmapFactory.decodeByteArray(image, 0, image.size)
        }
    }
}