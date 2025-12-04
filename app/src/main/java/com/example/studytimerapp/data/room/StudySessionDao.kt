package com.example.studytimerapp.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {
    @Query("SELECT * FROM study_sessions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getSessions(userId: String): Flow<List<StudySessionEntity>>

    @Insert
    suspend fun insertSession(session: StudySessionEntity)

    @Delete
    suspend fun deleteSession(session: StudySessionEntity)

    @Query("DELETE FROM study_sessions WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: String)

    // GOAL LUNAR
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMonthlyGoal(goal: MonthlyGoalEntity)

    @Query("SELECT * FROM monthly_goal WHERE userId = :userId LIMIT 1")
    suspend fun getMonthlyGoal(userId: String): MonthlyGoalEntity?

    @Query("SELECT * FROM monthly_goal WHERE userId = :userId LIMIT 1")
    fun getMonthlyGoalFlow(userId: String): Flow<MonthlyGoalEntity?>

    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM study_sessions WHERE userId = :userId AND strftime('%Y-%m', timestamp / 1000, 'unixepoch') = :yearMonth")
    suspend fun getMinutesThisMonth(userId: String, yearMonth: String): Int
}
