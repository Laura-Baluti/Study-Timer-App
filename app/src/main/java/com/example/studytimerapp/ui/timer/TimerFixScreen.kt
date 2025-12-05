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
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.content.Context
import kotlin.math.sqrt
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


    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val lightSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) }

    var isDark by remember { mutableStateOf(false) }
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val lux = event.values[0]
                isDark = lux < 10f // sub 30 lux = întuneric (poți ajusta valoarea)
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        lightSensor?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    // === 2. SHAKE TO PAUSE ===
    var shakeDetected by remember { mutableStateOf(false) }
    var lastShakeTime by remember { mutableStateOf(0L) }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    DisposableEffect(Unit) {
        val shakeListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val gForce = sqrt(x*x + y*y + z*z.toDouble()) / SensorManager.GRAVITY_EARTH
                    val now = System.currentTimeMillis()

                    if (gForce > 2.7 && now - lastShakeTime > 2000) { // shake puternic + anti-spam 2 sec
                        lastShakeTime = now
                        viewModel.startBreak(5)
                        shakeDetected = true
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        accelerometer?.let {
            sensorManager.registerListener(shakeListener, it, SensorManager.SENSOR_DELAY_GAME)
        }

        onDispose { sensorManager.unregisterListener(shakeListener) }
    }

    // Ascundem mesajul de shake după 1.5 sec
    if (shakeDetected) {
        LaunchedEffect(Unit) {
            delay(1500)
            shakeDetected = false
        }
    }




    val displayTime by viewModel.displayTime.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()
    val breakTimeLeft by viewModel.breakTimeLeft.collectAsState()
    val sessionFinished by viewModel.sessionFinished.collectAsState()

    val minutes = displayTime / 60
    val seconds = displayTime % 60
    val buttonColor = if (isDark) Color(0xFF1A3A1B) else Color(0xFF466531)
    val stopButtonColor = if (isDark) Color(0xFF1A3A1B) else Color(0xFF466531)
    Box(modifier = Modifier.fillMaxSize()) {
        // IMAGINE DINAMICĂ ÎN FUNCȚIE DE LUMINĂ
        Image(
            painter = painterResource(id = if (isDark) R.drawable.timerdark else R.drawable.timerlightt),
            contentDescription = "Timer Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay ușor ca să se vadă textul
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(if (isDark) 0x88000000 else 0x66000000))
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
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Pauză scurtă", fontFamily = CherryBomb, color = Color.White)
                }

                Button(
                    onClick = { viewModel.startBreak(10) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Pauză lungă", fontFamily = CherryBomb, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { viewModel.stopSession() },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = stopButtonColor)
            ) {
                Text("Stop Session", fontFamily = CherryBomb, color = Color.White, fontSize = 20.sp)
            }
        }

        // Feedback vizual la shake
        if (shakeDetected) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Card(modifier = Modifier.padding(top = 100.dp), colors = CardDefaults.cardColors(containerColor = Color(0xDDE5427E))) {
                    Text("Pauză activată prin shake!", fontFamily = CherryBomb, fontSize = 18.sp, color = Color.White, modifier = Modifier.padding(16.dp))
                }
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