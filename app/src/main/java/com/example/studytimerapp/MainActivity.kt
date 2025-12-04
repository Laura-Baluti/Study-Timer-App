package com.example.studytimerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studytimerapp.data.room.AppDatabase
import com.example.studytimerapp.ui.login.LoginScreen
import com.example.studytimerapp.ui.login.RegisterScreen
import com.example.studytimerapp.ui.home.HomeScreen
import com.example.studytimerapp.ui.timer.TimerFixScreen
import com.example.studytimerapp.ui.timer.CronometruLiberScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyTimerApp()
        }
    }
}

@Composable
fun StudyTimerApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") { // start cu login

        // LoginScreen
        composable("login") {
            LoginScreen(navController)
        }

        // RegisterScreen
        composable("register") {
            RegisterScreen(navController)
        }

        // HomeScreen
        composable("home") {
            HomeScreen(navController)
        }

        // Timer Fix
        composable("timerFix/{sessionName}/{minutes}") { backStackEntry ->
            val sessionName = backStackEntry.arguments?.getString("sessionName") ?: "Programare"
            val minutes = backStackEntry.arguments?.getString("minutes")?.toIntOrNull() ?: 25
            TimerFixScreen(navController, sessionName, minutes)
        }

        // Cronometru Liber
        composable("cronometruLiber/{sessionName}") { backStackEntry ->
            val sessionName = backStackEntry.arguments?.getString("sessionName") ?: "Matematica"
            CronometruLiberScreen(navController, sessionName)
        }
    }
}