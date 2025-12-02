package com.example.studytimerapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studytimerapp.data.room.StudyRepository

import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import androidx.lifecycle.viewModelScope
import com.example.studytimerapp.data.firebase.FirebaseAuthManager
import com.example.studytimerapp.data.room.AppDatabase
import com.example.studytimerapp.data.room.StudySessionEntity
import kotlinx.coroutines.flow.SharingStarted
class HomeViewModel(
    private val database: AppDatabase,
    private val authManager: FirebaseAuthManager = FirebaseAuthManager()
) : ViewModel() {

    private val userId = authManager.currentUser?.uid ?: ""

    val sessions = database.studySessionDao()
        .getSessions(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}