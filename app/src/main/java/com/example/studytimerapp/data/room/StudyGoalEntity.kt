package com.example.studytimerapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_goal")
data class MonthlyGoalEntity(
    @PrimaryKey val id: Long = 1L,           // mereu 1 → doar un goal activ
    val userId: String,
    val yearMonth: String,                   // ex: "2025-12"
    val targetMinutes: Int,
    var currentMinutes: Int = 0,
    val isCompleted: Boolean = false
)