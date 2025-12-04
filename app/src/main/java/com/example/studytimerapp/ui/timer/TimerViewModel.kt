// File: TimerViewModel.kt
package com.example.studytimerapp.ui.timer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studytimerapp.data.firebase.FirebaseAuthManager
import com.example.studytimerapp.data.room.AppDatabase
import com.example.studytimerapp.data.room.StudySessionEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import com.example.studytimerapp.data.room.StudyRepository
class TimerViewModel(
    context: Context,
    private val sessionName: String,
    private val sessionType: String,
    fixedMinutes: Int = 0  // 0 = cronometru liber, >0 = timer fix
) : ViewModel() {


    private val db = AppDatabase.getDatabase(context.applicationContext)
    private val authManager = FirebaseAuthManager()
    private val userId = authManager.currentUser?.uid ?: ""

    private val initialFixedSeconds = fixedMinutes * 60
    private var startTime = System.currentTimeMillis() // pentru cronometru liber

    // Stare UI
    private val _displayTime = MutableStateFlow(if (fixedMinutes > 0) initialFixedSeconds else 0)
    val displayTime = _displayTime.asStateFlow()

    private val _isRunning = MutableStateFlow(true)
    val isRunning = _isRunning.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()

    private val _breakTimeLeft = MutableStateFlow(0)
    val breakTimeLeft = _breakTimeLeft.asStateFlow()

    private val _sessionFinished = MutableStateFlow(false)
    val sessionFinished = _sessionFinished.asStateFlow()

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_isRunning.value && !_sessionFinished.value) {
                delay(1000)

                if (_isPaused.value) {
                    if (_breakTimeLeft.value > 0) {
                        _breakTimeLeft.value -= 1
                    } else {
                        _isPaused.value = false  // pauza s-a terminat automat
                    }
                } else {
                    // Timer Fix: scade
                    if (initialFixedSeconds > 0) {
                        if (_displayTime.value > 0) {
                            _displayTime.value -= 1
                        } else {
                            finishSession()
                        }
                    }
                    // Cronometru Liber: crește
                    else {
                        _displayTime.value += 1
                    }
                }
            }
        }
    }


    fun startBreak(minutes: Int) {
        if (!_isPaused.value) {
            _isPaused.value = true
            _breakTimeLeft.value = minutes * 60

        }
    }
    fun stopSession() {
        _isRunning.value = false
        finishSession()
    }

    private fun finishSession() {
        if (_sessionFinished.value) return
        _sessionFinished.value = true

        viewModelScope.launch {
            val actualMinutes = if (initialFixedSeconds > 0) {
                (initialFixedSeconds - _displayTime.value) / 60  // cât a lucrat efectiv
            } else {
                _displayTime.value / 60
            }.coerceAtLeast(0)

            val session = StudySessionEntity(
                userId = userId,
                name = sessionName,
                type = sessionType,
                durationMinutes = actualMinutes,
                timestamp = Date().time
            )
            db.studySessionDao().insertSession(session)
            val repository = StudyRepository(db.studySessionDao())
            repository.updateMonthlyGoalProgress(userId)
        }
    }

    fun resetAndGoHome(navigate: () -> Unit) {
        _sessionFinished.value = false
        _displayTime.value = if (initialFixedSeconds > 0) initialFixedSeconds else 0
        _isRunning.value = true
        _isPaused.value = false
        _breakTimeLeft.value = 0
        navigate()
    }
}