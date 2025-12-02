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
}
