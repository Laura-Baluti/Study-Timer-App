// TimerViewModelFactory.kt
package com.example.studytimerapp.ui.timer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TimerViewModelFactory(
    private val context: Context,
    private val sessionName: String,
    private val sessionType: String,
    private val fixedMinutes: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimerViewModel(context, sessionName, sessionType, fixedMinutes) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}