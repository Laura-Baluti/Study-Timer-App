package com.example.studytimerapp.ui.home

//Necesar pt a seta culorile la TextField-ul din dropdown.
import androidx.compose.material3.TextFieldDefaults

//folosit pt gradientul din background
import androidx.compose.foundation.background

//Importul fontului custom.
import com.example.studytimerapp.ui.theme.CherryBomb

//Pt Column, Row, Box, padding, fillMaxSize etc.
import androidx.compose.foundation.layout.*

//Folosit pentru colțurile butonului și cardului.
import androidx.compose.foundation.shape.RoundedCornerShape

//Importă tot din Material3: Card, Button, AlertDialog, Text, etc.
import androidx.compose.material3.*

//Necesare pentru state: remember, mutableStateOf, by.
import androidx.compose.runtime.*

//Pentru Modifiers în general.
import androidx.compose.ui.Modifier

//În Android și Jetpack Compose dimensiunile UI nu se măsoară în pixeli simpli,
// pentru că device-urile au densități diferite.
//De aceea se folosesc unități speciale:
//dp se folosește pentru dimensiuni de layout: padding, width etc
import androidx.compose.ui.unit.dp
//sp se folosește pentru mărimea textului.
//De ex daca cineva are setare pe tel large text se adapteaza
import androidx.compose.ui.unit.sp

//Pentru "centerHorizontally" etc.
import androidx.compose.ui.Alignment

//Folosite pt gradient și culori.
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

//Folosit la titlu.
import androidx.compose.ui.text.font.FontWeight

//Necesare pt dropdown-ul custom Material3.
import androidx.compose.material3.TextField
import androidx.compose.material3.ExposedDropdownMenuBox

//pt imagini
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.example.studytimerapp.R
import androidx.compose.ui.draw.rotate

import androidx.navigation.NavController

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.ui.unit.DpOffset
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import java.text.SimpleDateFormat
import androidx.compose.ui.graphics.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studytimerapp.data.room.AppDatabase
import com.example.studytimerapp.data.room.StudySessionEntity
import java.util.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.layout.ContentScale
data class StudySession(val name: String, val type: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current

    val viewModel: HomeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(context.applicationContext) as T
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.refreshGoal()
    }


    var selectedMinutes by remember { mutableStateOf("25") }
    // Sesiunile reale din Room
    val monthlyGoal by viewModel.monthlyGoal.collectAsState()
    val roomSessions by viewModel.sessions.collectAsState(initial = emptyList())

     // Convertim în formatul tău vechi doar pentru dropdown
    val dropdownSessions = remember(roomSessions) {
        roomSessions.map { StudySession(it.name, it.type) }
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedSession by remember { mutableStateOf<StudySession?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var newSessionName by remember { mutableStateOf("") }
    var newSessionType by remember { mutableStateOf("Timer Fix") }
    var profileMenuExpanded by remember { mutableStateOf(false) }
    var showSessionDetails by remember { mutableStateOf<StudySessionEntity?>(null) }
    var showGoalDialog by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        // IMAGINE DE FUNDAL COMPLETĂ
        Image(
            painter = painterResource(id = R.drawable.home), // ← imaginea ta
            contentDescription = "Home Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay ușor ca să se vadă textul și cardurile clar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x22000000)) // negru semi-transparent – ajustează dacă vrei
        )

        // Buton profil
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(30.dp)) {
            IconButton(
                onClick = { profileMenuExpanded = !profileMenuExpanded },
                modifier = Modifier.size(48.dp).background(Color(0x44FFFFFF), CircleShape)
            ) {
                Icon(Icons.Default.AccountCircle, "Profil", tint = Color(0xFFFFE4D6), modifier = Modifier.size(32.dp))
            }

            DropdownMenu(
                expanded = profileMenuExpanded,
                onDismissRequest = { profileMenuExpanded = false },
                offset = DpOffset(x = (-16).dp, y = 8.dp),
                modifier = Modifier.background(Color(0xFFC0899C)).width(220.dp)
            ) {
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                            Icon(Icons.Default.Logout, null, tint = Color(0xFFFFE4D6), modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(12.dp))
                            Text("Deconectare", color = Color(0xFFFFE4D6), fontFamily = CherryBomb, fontSize = 18.sp)
                        }
                    },
                    onClick = {
                        profileMenuExpanded = false
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Study With Me",
                fontFamily = CherryBomb,
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.ui.text.TextStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFF1CCBE), Color(0xFFE7CABC),
                            Color(0xFFE7ABBE), Color(0xFFEEB3C7), Color(0xFFC9869D)
                        )
                    ),
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = androidx.compose.ui.geometry.Offset(6f, 9f),
                        blurRadius = 16f
                    )
                ),
                modifier = Modifier.padding(top = 20.dp)
            )
            monthlyGoal?.let { goal ->
                Spacer(Modifier.height(24.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0x44FFFFFF)),
                    shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Obiectiv lunar", color = Color(0xFFFCCEE0), fontFamily = CherryBomb, fontSize = 22.sp)
                        Spacer(Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { goal.currentMinutes.toFloat() / goal.targetMinutes.coerceAtLeast(1) },
                            modifier = Modifier.fillMaxWidth().height(20.dp).clip(RoundedCornerShape(10.dp)),
                            color = if (goal.isCompleted) Color(0xFF009688) else Color(0xFFB43B64),
                            trackColor = Color(0xFFF1B5CB)
                        )

                        Spacer(Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${goal.currentMinutes} / ${goal.targetMinutes} min", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            if (goal.isCompleted) Text("COMPLETAT!", color = Color(0xFF009688), fontFamily = CherryBomb, fontSize = 20.sp)
                        }

                        // BUTOANE GOAL – VARIANTA 100% COMPATIBILĂ
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                        ) {
                            Text("Alege obiectivul lunar", color = Color.White.copy(0.9f), fontSize = 14.sp)

                            Spacer(Modifier.height(10.dp))

                            // Row corect – fără horizontalAlignment
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                listOf(900, 1200, 1800, 2400).forEach { mins ->
                                    FilterChip(
                                        selected = goal.targetMinutes == mins,
                                        onClick = { viewModel.setGoal(mins) },
                                        label = { Text("${mins / 60}h") },
                                        colors = FilterChipDefaults.filterChipColors(
                                            containerColor = Color(0xFFF1B7CB),
                                            labelColor = Color(0xFF44051C),
                                            selectedContainerColor = Color(0xFFEFB9CB),
                                            selectedLabelColor = Color.White,
                                            disabledContainerColor = Color.Transparent,
                                            disabledLabelColor = Color.White.copy(alpha = 0.38f)
                                        )
                                    )
                                }
                            }

                            Spacer(Modifier.height(10.dp))

                            FilterChip(
                                selected = false,
                                onClick = { showGoalDialog = true },
                                label = { Text("Personalizat") },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color(0xFFF1B7CB),
                                    labelColor = Color(0xFF44051C)
                                )
                            )
                        }
                        // DIALOG PERSONALIZAT
                        if (showGoalDialog) {
                            var customMinutesText by remember { mutableStateOf(goal.targetMinutes.toString()) }

                            AlertDialog(
                                onDismissRequest = { showGoalDialog = false },
                                title = {
                                    Text("Obiectiv lunar personalizat", fontFamily = CherryBomb, color = Color(0xFFFFE4D6), fontSize = 20.sp)
                                },
                                text = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Câte minute vrei luna asta?", color = Color.White, fontSize = 16.sp)
                                        Spacer(Modifier.height(16.dp))

                                        OutlinedTextField(
                                            value = customMinutesText,
                                            onValueChange = { input ->
                                                if (input.all { it.isDigit() } || input.isEmpty()) {
                                                    customMinutesText = input
                                                }
                                            },
                                            label = { Text("Minute") },
                                            singleLine = true,
                                            colors = TextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                cursorColor = Color(0xFFFFE4D6),
                                                focusedContainerColor = Color(0xFFF8E1E7),
                                                unfocusedContainerColor = Color(0xFFF1CCDA),
                                                focusedIndicatorColor = Color(0xFFFFE4D6)
                                            ),
                                            modifier = Modifier.width(200.dp)
                                        )
                                    }
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        val minutes = customMinutesText.toIntOrNull() ?: 1800
                                        if (minutes > 0) {
                                            viewModel.setGoal(minutes.coerceAtMost(999999))
                                            showGoalDialog = false
                                        }
                                    }) {
                                        Text("Salvează", color = Color(0xFFFFE4D6), fontFamily = CherryBomb)
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showGoalDialog = false }) {
                                        Text("Anulează", color = Color(0xFFFFE4D6))
                                    }
                                },
                                containerColor = Color(0xFFD36890),
                                shape = RoundedCornerShape(24.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x55FFFFFF)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = "Sesiuni trecute",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Istoric sesiuni", color = Color(0xFF600B26)) },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF600B26),
                            unfocusedTextColor = Color(0xFF600B26),
                            disabledTextColor = Color(0xFF600B26),
                            cursorColor = Color(0xFFF3E6E6),
                            focusedContainerColor = Color(0x33FFFFFF),
                            unfocusedContainerColor = Color(0x22FFFFFF),
                            disabledContainerColor = Color(0x22FFFFFF),
                            focusedIndicatorColor = Color(0xFFFFE4D6),
                            unfocusedIndicatorColor = Color(0x99FFE4D6),
                            disabledIndicatorColor = Color.Transparent,
                            focusedLabelColor = Color(0xFFF1CAD2),
                            unfocusedLabelColor = Color(0xCCFFE4D6)
                        ),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        if (dropdownSessions.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Nicio sesiune încă...", color = Color(0xFF540A28).copy(alpha = 0.7f)) },
                                onClick = { expanded = false }
                            )
                        } else {
                            dropdownSessions.forEach { session ->
                                val fullSession = roomSessions.find { it.name == session.name && it.type == session.type }
                                DropdownMenuItem(
                                    text = { Text("${session.name} (${session.type})") },
                                    onClick = {
                                        expanded = false
                                        fullSession?.let { showSessionDetails = it }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF93314F)),
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("New Session", fontFamily = CherryBomb, fontSize = 20.sp, color = Color.White)
            }
        }

        if (showDialog) {
            AlertDialog(onDismissRequest = { showDialog = false },
                title = { Text("New Study Session", fontFamily = CherryBomb, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFE4D6)) },
                text = {
                    Column(modifier = Modifier.background(Brush.verticalGradient(listOf(Color(
                        0xFFA25772
                    ), Color(
                        0xFFE8A6BA
                    )
                    )), RoundedCornerShape(16.dp)).padding(20.dp)) {
                        OutlinedTextField(value = newSessionName, onValueChange = { newSessionName = it },
                            label = { Text("Nume sesiune", color = Color(0xFFFFE4D6)) }, shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                                cursorColor = Color(0xFFFFE4D6), focusedContainerColor = Color(0x33FFFFFF),
                                unfocusedContainerColor = Color(0x22FFFFFF), focusedIndicatorColor = Color(0xFFFFE4D6)),
                            modifier = Modifier.fillMaxWidth())

                        Spacer(Modifier.height(20.dp))
                        Text("Tip sesiune:", color = Color(0xFFFFE4D6), fontFamily = CherryBomb, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = newSessionType == "Timer Fix", onClick = { newSessionType = "Timer Fix" })
                            Text("Timer Fix", color = Color.White, modifier = Modifier.padding(start = 8.dp))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = newSessionType == "Cronometru Liber", onClick = { newSessionType = "Cronometru Liber" })
                            Text("Cronometru Liber", color = Color.White, modifier = Modifier.padding(start = 8.dp))
                        }

                        if (newSessionType == "Timer Fix") {
                            Spacer(Modifier.height(20.dp))
                            Text("Câte minute vrei să înveți?", color = Color(0xFFFFE4D6), fontFamily = CherryBomb, fontSize = 18.sp)
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = selectedMinutes,
                                onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) selectedMinutes = it },
                                label = { Text("Minute (ex: 25, 40, 60)", color = Color(0xFFFFE4D6)) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                                    cursorColor = Color(0xFFFFE4D6), focusedContainerColor = Color(
                                        0x65FFFFFF
                                    ),
                                    unfocusedContainerColor = Color(0x22FFFFFF), focusedIndicatorColor = Color(0xFFFFE4D6))
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newSessionName.isNotBlank()) {
                            showDialog = false
                            if (newSessionType == "Timer Fix") {
                                val mins = selectedMinutes.toIntOrNull() ?: 25
                                navController.navigate("timerFix/$newSessionName/$mins")
                            } else {
                                navController.navigate("cronometruLiber/$newSessionName")
                            }
                            newSessionName = ""; newSessionType = "Timer Fix"; selectedMinutes = "25"
                        }
                    }) { Text("Start", fontFamily = CherryBomb, fontSize = 20.sp, color = Color.White) }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false; newSessionName = ""; selectedMinutes = "25" }) {
                        Text("Anulează", color = Color(0xFFFFE4D6), fontFamily = CherryBomb)
                    }
                },
                containerColor = Color.Transparent,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.background(
                    Brush.verticalGradient(listOf(Color(0xFF671330).copy(0.95f), Color(0xFF805562))),
                    RoundedCornerShape(20.dp)).padding(8.dp))
        }
        // === POPUP DETALII SESIUNE ===
        showSessionDetails?.let { session ->
            AlertDialog(onDismissRequest = { showSessionDetails = null },
                title = { Text(session.name, fontFamily = CherryBomb, fontSize = 26.sp, color = Color(0xFFFFE4D6), fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.background(Brush.verticalGradient(listOf(Color(
                        0xFFECCAD6
                    ), Color(
                        0xFFD76F92
                    )
                    )), RoundedCornerShape(16.dp)).padding(20.dp)) {
                        DetailRow("Tip", session.type)
                        DetailRow("Timp învățat", "${session.durationMinutes} minute")
                        val df = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        val tf = SimpleDateFormat("HH:mm", Locale.getDefault())
                        DetailRow("Data", df.format(Date(session.timestamp)))
                        DetailRow("Ora", tf.format(Date(session.timestamp)))
                    }
                },
                confirmButton = { TextButton(onClick = { showSessionDetails = null }) {
                    Text("Închide", color = Color(0xFFFFE4D6), fontFamily = CherryBomb, fontSize = 18.sp)
                }},
                containerColor = Color(0xFFC76587).copy(alpha = 0.98f),
                shape = RoundedCornerShape(20.dp))
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("$label:", color = Color(0xFFFFE4D6), fontFamily = CherryBomb, fontSize = 18.sp)
        Text(value, color = Color.White, fontFamily = CherryBomb, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}