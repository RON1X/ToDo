package com.eachubkov.todoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eachubkov.todoapp.data.entity.NotesEntity
import com.eachubkov.todoapp.utils.Constants.Companion.DATABASE_NAME
import dev.matrix.roomigrant.GenerateRoomMigrations

@Database(
    entities = [NotesEntity::class],
    version = 1
)
@TypeConverters(Converter::class)
@GenerateRoomMigrations
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun toDoDao(): ToDoDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoDatabase? = null

        fun getDatabase(context: Context): ToDoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    DATABASE_NAME
                ).addMigrations(*ToDoDatabase_Migrations.build()).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}