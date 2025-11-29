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
import com.example.studytimerapp.ui.theme.CherryBomb
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CronometruLiberScreen(
    navController: NavController,
    sessionName: String = "Matematica"
) {

    var elapsedTime by remember { mutableIntStateOf(0) } // timp principal în secunde
    var isPaused by remember { mutableStateOf(false) } // pauză activă sau nu
    var pauseTime by remember { mutableIntStateOf(0) } // pauza curentă în secunde
    var sessionEnded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Countdown principal + pauză
    LaunchedEffect(Unit) {
        while (!sessionEnded) {
            delay(1000)
            if (!isPaused) {
                elapsedTime += 1
            } else {
                if (pauseTime > 0) {
                    pauseTime -= 1
                }
                if (pauseTime <= 0) isPaused = false
            }
        }
    }

    val fullCozyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2A0800),
            Color(0xFF774936),
            Color(0xFF8A5A44),
            Color(0xFF9D6B53),
            Color(0xFFB07D62),
            Color(0xFFC38E70),
            Color(0xFFD69F7E),
            Color(0xFFE6B8A2),
            Color(0xFFEDC4B3)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fullCozyGradient)
    ) {
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

            val minutes = elapsedTime / 60
            val seconds = elapsedTime % 60
            Text(
                text = String.format(Locale.US, "%02d:%02d", minutes, seconds),
                fontFamily = CherryBomb,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFE4D6)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Pauza
            if (isPaused) {
                val pauseMinutes = pauseTime / 60
                val pauseSeconds = pauseTime % 60
                Text(
                    text = "Pauză: ${String.format(Locale.US, "%02d:%02d", pauseMinutes, pauseSeconds)}",
                    fontFamily = CherryBomb,
                    fontSize = 24.sp,
                    color = Color(0xFFFFE4D6)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }


            // Butoanele de pauză
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        if (!isPaused) {
                            isPaused = true
                            pauseTime = 1 * 60
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB07D62))
                ) {
                    Text("Pauză scurtă", fontFamily = CherryBomb, color = Color.White)
                }

                Button(
                    onClick = {
                        if (!isPaused) {
                            isPaused = true
                            pauseTime = 10 * 60
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB07D62))
                ) {
                    Text("Pauză lungă", fontFamily = CherryBomb, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Buton de terminare sesiune
            Button(
                onClick = { sessionEnded = true },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC38E70))
            ) {
                Text("End Session", fontFamily = CherryBomb, color = Color.White)
            }
        }

        // Popup la final
        if (sessionEnded) {
            AlertDialog(
                onDismissRequest = {
                    sessionEnded = false
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                title = { Text("Sesiune terminată!", fontFamily = CherryBomb, color = Color(0xFFFFE4D6)) },
                text = { Text("Felicitări! Ai terminat sesiunea.", color = Color.White) },
                confirmButton = {
                    TextButton(onClick = {
                        sessionEnded = false
                        navController.navigate("home") { popUpTo("home") { inclusive = true } }
                    }) {
                        Text("OK", fontFamily = CherryBomb, color = Color(0xFFFFE4D6))
                    }
                },
                containerColor = Color(0xFF774936)
            )
        }
    }
}
