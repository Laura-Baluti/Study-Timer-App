// ui/home/HomeViewModel.kt
package com.example.studytimerapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studytimerapp.data.firebase.FirebaseAuthManager
import com.example.studytimerapp.data.room.AppDatabase
import com.example.studytimerapp.data.room.MonthlyGoalEntity
import com.example.studytimerapp.data.room.StudyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.content.Context
import com.example.studytimerapp.data.room.StudySessionEntity

class HomeViewModel(context: Context) : ViewModel() {

    private val repository = StudyRepository(AppDatabase.getDatabase(context).studySessionDao())
    private val userId get() = FirebaseAuthManager().currentUser?.uid ?: "guest_user"

    val sessions = repository.getSessions(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val monthlyGoal = repository.getMonthlyGoalFlow(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            repository.getOrCreateMonthlyGoal(userId, 1800)
            repository.updateMonthlyGoalProgress(userId)
        }
    }

    fun refreshGoal() {
        viewModelScope.launch {
            repository.updateMonthlyGoalProgress(userId)
        }
    }

    // ACUM MERGE DE FIECARE DATĂ
    fun setGoal(minutes: Int) {
        viewModelScope.launch {
            repository.setMonthlyGoalTarget(userId, minutes)
            repository.updateMonthlyGoalProgress(userId)
        }
    }
}