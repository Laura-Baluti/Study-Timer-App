package com.example.studytimerapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [StudySessionEntity::class, MonthlyGoalEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studySessionDao(): StudySessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE monthly_goal (
                        id INTEGER PRIMARY KEY NOT NULL DEFAULT 1,
                        userId TEXT NOT NULL,
                        yearMonth TEXT NOT NULL,
                        targetMinutes INTEGER NOT NULL,
                        currentMinutes INTEGER NOT NULL DEFAULT 0,
                        isCompleted INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())
            }
        }
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "study_database"
                )
                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration() // doar pentru test – șterge mai târziu dacă vrei
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
