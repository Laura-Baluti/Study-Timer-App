package com.example.studytimerapp.data.room

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

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

    // === GOAL LUNAR ===
    private fun getCurrentYearMonth(): String =
        SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())

    suspend fun setMonthlyGoalTarget(userId: String, newTarget: Int) {
        val currentMonth = getCurrentYearMonth()
        val currentGoal = dao.getMonthlyGoal(userId)

        val updatedGoal = if (currentGoal != null && currentGoal.yearMonth == currentMonth) {
            currentGoal.copy(targetMinutes = newTarget, isCompleted = false)
        } else {
            MonthlyGoalEntity(
                userId = userId,
                yearMonth = currentMonth,
                targetMinutes = newTarget,
                currentMinutes = 0,
                isCompleted = false
            )
        }
        dao.upsertMonthlyGoal(updatedGoal)
    }

    suspend fun updateMonthlyGoalProgress(userId: String) {
        val goal = dao.getMonthlyGoal(userId) ?: return
        if (goal.yearMonth != getCurrentYearMonth()) return

        val total = dao.getMinutesThisMonth(userId, goal.yearMonth)
        val updated = goal.copy(currentMinutes = total, isCompleted = total >= goal.targetMinutes)
        dao.upsertMonthlyGoal(updated)
    }

    suspend fun getOrCreateMonthlyGoal(userId: String, targetMinutes: Int = 1800): MonthlyGoalEntity {
        val currentMonth = getCurrentYearMonth()
        var goal = dao.getMonthlyGoal(userId)

        if (goal == null || goal.yearMonth != currentMonth) {
            goal = MonthlyGoalEntity(
                userId = userId,
                yearMonth = currentMonth,
                targetMinutes = targetMinutes,
                currentMinutes = 0,
                isCompleted = false
            )
            dao.upsertMonthlyGoal(goal)
        }
        return goal
    }

    fun getMonthlyGoalFlow(userId: String): Flow<MonthlyGoalEntity?> =
        dao.getMonthlyGoalFlow(userId)
}
