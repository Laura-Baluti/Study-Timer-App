package com.example.studytimerapp.data.room

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "study_app_db"
            ).build().also { INSTANCE = it }
        }
    }
}
