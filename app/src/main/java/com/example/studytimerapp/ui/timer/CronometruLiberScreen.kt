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
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studytimerapp.ui.theme.CherryBomb
import java.util.Locale
import androidx.compose.ui.platform.LocalContext


import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.example.studytimerapp.R
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CronometruLiberScreen(
    navController: NavController,
    sessionName: String
) {
    val context = LocalContext.current

    val viewModel: TimerViewModel = viewModel(
        factory = TimerViewModelFactory(
            context = context,
            sessionName = sessionName,
            sessionType = "Cronometru Liber",
            fixedMinutes = 0
        )
    )

    val displayTime by viewModel.displayTime.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()
    val breakTimeLeft by viewModel.breakTimeLeft.collectAsState()
    val sessionFinished by viewModel.sessionFinished.collectAsState()

    val minutes = displayTime / 60
    val seconds = displayTime % 60

    Box(modifier = Modifier.fillMaxSize()) {
        //  IMAGINE DE FUNDAL COMPLET
        Image(
            painter = painterResource(id = R.drawable.cronlight), // pune-ți imaginea aici
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay ușor ca să se vadă textul (poți șterge dacă imaginea e întunecată)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x11000000)) // negru semi-transparent – ajustează dacă vrei
        )


        // TOT CODUL TĂU RĂMÂNE EXACT AȘA CUM ERA
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
                color = Color(0xFFFFE4D6)
            )

            Spacer(modifier = Modifier.height(24.dp))

            val minutes = displayTime / 60
            val seconds = displayTime % 60
            Text(
                text = String.format(Locale.US, "%02d:%02d", minutes, seconds),
                fontFamily = CherryBomb,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFE4D6)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Afișează pauza dacă e activă
            if (isPaused && breakTimeLeft > 0) {
                val breakMin = breakTimeLeft / 60
                val breakSec = breakTimeLeft % 60
                Text(
                    text = "Pauză: ${String.format(Locale.US, "%02d:%02d", breakMin, breakSec)}",
                    fontFamily = CherryBomb,
                    fontSize = 28.sp,
                    color = Color(0xFFFFE4D6)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            // Butoane pauză
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { viewModel.startBreak(5) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5427E))
                ) {
                    Text("Pauză scurtă", fontFamily = CherryBomb, color = Color.White)
                }

                Button(
                    onClick = { viewModel.startBreak(10) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5427E))
                ) {
                    Text("Pauză lungă", fontFamily = CherryBomb, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Stop Session
            Button(
                onClick = { viewModel.stopSession() },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB64E74))
            ) {
                Text("Stop Session", fontFamily = CherryBomb, color = Color.White, fontSize = 20.sp)
            }
        }

        // AlertDialog la finalizare
        if (sessionFinished) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text("Sesiune terminată!", fontFamily = CherryBomb, color = Color(0xFFFFE4D6))
                },
                text = {
                    Text(
                        "Felicitări! Ai învățat $minutes minute și ai salvat sesiunea.",
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
                        Text("OK", fontFamily = CherryBomb, color = Color(0xFFFFE4D6))
                    }
                },
                containerColor = Color(0xFF7A253E),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}