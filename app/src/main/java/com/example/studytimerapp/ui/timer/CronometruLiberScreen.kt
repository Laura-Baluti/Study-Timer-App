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
import android.content.Context
import kotlinx.coroutines.delay
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt
import kotlin.math.sqrt
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

    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val lightSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) }

    // Detectăm întunericul
    var isDark by remember { mutableStateOf(false) }
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val lux = event.values[0]
                isDark = lux < 10f
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        lightSensor?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        onDispose { sensorManager.unregisterListener(listener) }
    }

    // === SHAKE TO PAUSE — AGITĂ TELEFONUL → PAUZĂ SCURTĂ AUTOMATĂ ===
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

                    val gForce = kotlin.math.sqrt((x*x + y*y + z*z).toDouble()) / SensorManager.GRAVITY_EARTH
                    val now = System.currentTimeMillis()

                    if (gForce > 2.7 && now - lastShakeTime > 2000) { // shake puternic + anti-spam 2 sec
                        lastShakeTime = now
                        viewModel.startBreak(5)  // PAUZĂ SCURTĂ AUTOMATĂ
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

    // Ascundem mesajul după 1.5 sec
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

    // CULORI DINAMICE PENTRU BUTOANE
    val buttonColor = if (isDark) Color(0xFF56182F) else Color(0xFFE5427E)
    val stopButtonColor = if (isDark) Color(0xFF5D2C3B) else Color(0xFFB64E74)
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = if (isDark) R.drawable.crondark else R.drawable.cronlight),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay adaptiv
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(if (isDark) 0x88000000 else 0x66000000))
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

            // Stop Session
            Button(
                onClick = { viewModel.stopSession() },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = stopButtonColor)
            ) {
                Text("Stop Session", fontFamily = CherryBomb, color = Color.White, fontSize = 20.sp)
            }
        }
        if (shakeDetected) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Card(
                    modifier = Modifier.padding(top = 100.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xDDE5427E))
                ) {
                    Text(
                        "Pauză activată prin shake!",
                        fontFamily = CherryBomb,
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
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