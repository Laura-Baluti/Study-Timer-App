package com.example.studytimerapp.data.room

import kotlinx.coroutines.flow.Flow

class StudyRepository(private val dao: StudySessionDao) {

    fun getSessions(userId: String): Flow<List<StudySessionEntity>> =
        dao.getSessions(userId)

    suspend fun addSession(
        userId: String,
        name: String,
        type: String,
        durationMinutes: Int
    ) {
        dao.insertSession(
            StudySessionEntity(
                userId = userId,
                name = name,
                type = type,
                durationMinutes = durationMinutes,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}
