package com.example.studytimerapp.ui.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studytimerapp.ui.theme.CherryBomb
import kotlinx.coroutines.delay
import androidx.navigation.NavController
import java.util.Locale
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.example.studytimerapp.data.firebase.FirebaseAuthManager
import com.example.studytimerapp.data.room.AppDatabase
import com.example.studytimerapp.data.room.StudySessionEntity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studytimerapp.ui.home.HomeViewModel
import java.util.Date
import androidx.compose.ui.platform.LocalContext


import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.example.studytimerapp.R
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerFixScreen(
    navController: NavController,  // adăugat
    sessionName: String,
    sessionMinutes: Int
) {
    val context = LocalContext.current

    val viewModel: TimerViewModel = viewModel(
        factory = TimerViewModelFactory(
            context = context,
            sessionName = sessionName,
            sessionType = "Timer Fix",
            fixedMinutes = sessionMinutes
        )
    )

    val displayTime by viewModel.displayTime.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()
    val breakTimeLeft by viewModel.breakTimeLeft.collectAsState()
    val sessionFinished by viewModel.sessionFinished.collectAsState()

    val minutes = displayTime / 60
    val seconds = displayTime % 60

    Box(modifier = Modifier.fillMaxSize()) {
        // IMAGINE DE FUNDAL COMPLETĂ
        Image(
            painter = painterResource(id = R.drawable.timerlightt), // ← imaginea ta aici
            contentDescription = "Timer Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay ușor ca să se vadă textul clar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x22000000)) // negru semi-transparent – ajustează dacă vrei
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sesiune: $sessionName",
                fontFamily = CherryBomb,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF)
            )

            Spacer(modifier = Modifier.height(24.dp))

            val minutes = displayTime / 60
            val seconds = displayTime % 60
            Text(
                text = String.format(Locale.US, "%02d:%02d", minutes, seconds),
                fontFamily = CherryBomb,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isPaused && breakTimeLeft > 0) {
                val breakMin = breakTimeLeft / 60
                val breakSec = breakTimeLeft % 60
                Text(
                    text = "Pauză: ${String.format(Locale.US, "%02d:%02d", breakMin, breakSec)}",
                    fontFamily = CherryBomb,
                    fontSize = 28.sp,
                    color = Color(0xFFFFFFFF)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            // Butoanele de pauză
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { viewModel.startBreak(5) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF466531))
                ) {
                    Text("Pauză scurtă", fontFamily = CherryBomb, color = Color.White)
                }

                Button(
                    onClick = { viewModel.startBreak(10) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF466531))
                ) {
                    Text("Pauză lungă", fontFamily = CherryBomb, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { viewModel.stopSession() },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF466531))
            ) {
                Text("Stop Session", fontFamily = CherryBomb, color = Color.White, fontSize = 20.sp)
            }
        }

        if (sessionFinished) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text("Sesiune terminată!", fontFamily = CherryBomb, color = Color(0xFFFFFFFF))
                },
                text = {
                    Text(
                        "Felicitări! Ai terminat sesiunea de $sessionMinutes minute.",
                        color = Color.White,
                        fontFamily = CherryBomb
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.resetAndGoHome {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }) {
                        Text("OK", fontFamily = CherryBomb, color = Color(0xFFFFFFFF))
                    }
                },
                containerColor = Color(0xFF466531),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}