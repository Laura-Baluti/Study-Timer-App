package com.example.studytimerapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val name: String,
    val type: String,
    val durationMinutes: Int,
    val timestamp: Long
)