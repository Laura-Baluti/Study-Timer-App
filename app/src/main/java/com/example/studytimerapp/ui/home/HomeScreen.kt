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

import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.geometry.Offset
// Model pt sesiunea de studiu
data class StudySession(
    val name: String,
    val type: String // "Timer Fix" sau "Cronometru Liber"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {

    var sessions by remember { mutableStateOf(
        listOf(
            StudySession("Programare", "Timer Fix"),
            StudySession("Matematica", "Cronometru Liber")
        )
    )}

    var expanded by remember { mutableStateOf(false) }
    var selectedSession by remember { mutableStateOf<StudySession?>(null) }

    var showDialog by remember { mutableStateOf(false) }
    var newSessionName by remember { mutableStateOf("") }
    var newSessionType by remember { mutableStateOf("Timer Fix") }

    var profileMenuExpanded by remember { mutableStateOf(false) }
    //crearea gradientului
    val fullCozyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2A0800),
            Color(0xFF774936),
            Color(0xFF8A5A44),
            Color(0xFF9D6B53),
            Color(0xFFB07D62),
            Color(0xFFB07D62),
            Color(0xFFC38E70),
            Color(0xFFCD9777),
            Color(0xFFD69F7E),
            Color(0xFFE6B8A2),
            Color(0xFFE6B8A2),
            Color(0xFFEDC4B3),
            Color(0xFFFFFFFF),
            Color(0xFFFFFFFF),
            Color(0xFFFFFFFF),
            Color(0xFFFFFFFF),
            Color(0xFFFFFFFF)// optional: deep mahogany accent dramatic
        )
    )

    //container care permite suprapunerea elementelor (z-index).
    Box(
        modifier = Modifier
            //ocupă tot ecranul.
            .fillMaxSize()
            //gradientul
            .background(fullCozyGradient)
    ) {
        Image(
            painter = painterResource(id = R.drawable.coffee2),
            contentDescription = "Sticker 1",
            modifier = Modifier
                .size(220.dp) // dimensiune imagine
                .align(Alignment.BottomStart) // poziționare
                .padding(16.dp)
                .rotate(-20f)// distanță față de margine
        )

        // Imaginea 2 – colț dreapta jos
        Image(
            painter = painterResource(id = R.drawable.cros),
            contentDescription = "Sticker 2",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )

        // === BUTON PROFIL (colț dreapta-sus) ===
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(30.dp)

        ) {
            // Butonul de profil

            IconButton(
                onClick = { profileMenuExpanded = !profileMenuExpanded },
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0x44FFFFFF), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profil",
                    tint = Color(0xFFFFE4D6),
                    modifier = Modifier.size(32.dp)
                )
            }

            // Dropdown-ul ancorat manual la buton
            DropdownMenu(
                expanded = profileMenuExpanded,
                onDismissRequest = { profileMenuExpanded = false },
                offset = DpOffset(x = (-16).dp, y = 8.dp), // aliniere perfectă sub buton
                modifier = Modifier
                    .background(Color(0xFF9D6B53))
                    .width(220.dp)
            ) {
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Logout,
                                contentDescription = null,
                                tint = Color(0xFFFFE4D6),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "Deconectare",
                                color = Color(0xFFFFE4D6),
                                fontFamily = CherryBomb,
                                fontSize = 18.sp
                            )
                        }
                    },
                    onClick = {
                        profileMenuExpanded = false
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true } // șterge tot stack-ul
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        //În interior, Column aranjează vertical toate elementele:
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            //centrăm pe orizontală
            horizontalAlignment = Alignment.CenterHorizontally,
            //spațiu între elemente (dropdown, titlu, buton etc.)
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Titlul aplicatiei
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Study With Me",
                fontFamily = CherryBomb,
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFF1DDBE),
                            Color(0xFFF1BFA6),
                            Color(0xFFF1D298),
                            Color(0xFFF8BEA0),
                            Color(0xFFF6CC88)
                        )
                    ),
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(6f, 9f),
                        blurRadius = 16f
                    )
                ),
                modifier = Modifier.padding(top = 20.dp)
            )
            // Dropdown pentru sesiunile existente
            Spacer(modifier = Modifier.height(10.dp))
            //cardul incadreaza dropdown-ul
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x55FFFFFF)), // translucid
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),

            ) {
                //container special Material3 pentru dropdown
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = selectedSession?.name ?: "Selectează sesiunea",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Sesiuni trecute", color = Color(0xFFFFE4D6)) },
                            //personalizează culorile: fundal, text, cursor, indicator.
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF6E3629),
                                unfocusedTextColor = Color(0xFF2A0800),
                                disabledTextColor = Color(0xFF2A0800),
                                cursorColor = Color(0xFFFFE4D6),

                                focusedContainerColor = Color(0x33FFFFFF),  // fundal semi-transparent
                                unfocusedContainerColor = Color(0x22FFFFFF),
                                disabledContainerColor = Color(0x22FFFFFF),

                                focusedIndicatorColor = Color(0xFFFFE4D6),   // linia de jos când e focus
                                unfocusedIndicatorColor = Color(0x99FFE4D6),
                                disabledIndicatorColor = Color.Transparent,

                                focusedLabelColor = Color(0xFFFFE4D6),
                                unfocusedLabelColor = Color(0xCCFFE4D6),

                                errorCursorColor = Color.Red,
                                errorIndicatorColor = Color.Red
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        //lista dropdown-ului care se deschide când expanded = true.
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            sessions.forEach { session ->
                                DropdownMenuItem(
                                    //fiecare element din listă;
                                    //la click setează selectedSession și închide dropdown-ul.
                                    text = { Text(session.name + " (${session.type})") },
                                    onClick = {
                                        selectedSession = session
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            Spacer(modifier = Modifier.height(10.dp))
            //Buton nouă sesiune
            //Butonul deschide dialogul pentru adăugarea unei sesiuni noi.
            Button(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC38E70)),
                modifier = Modifier
                    .fillMaxWidth(0.5f) // buton mai scurt, centrat
            ) {
                Text(
                    text = "New Session",
                    fontFamily = CherryBomb,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }


        // Dialog pentru inceperea unei sesiuni
        if (showDialog) {
            //fereastra popup pentru adăugarea sesiunii.
            AlertDialog(
                onDismissRequest = { showDialog = false },
                //butonul Add
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newSessionName.isNotBlank()) {
                                sessions = sessions + StudySession(newSessionName, newSessionType)
                                showDialog = false

                                if (newSessionType == "Timer Fix") {
                                    navController.navigate("timerFix/$newSessionName/25") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                } else {
                                    navController.navigate("cronometruLiber/$newSessionName") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                }

                                newSessionName = ""
                                newSessionType = "Timer Fix"
                            }
                        }
                    ) {
                        Text(
                            text = "New Session",
                            fontFamily = CherryBomb,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }

                    },
                //butonul „Cancel” care închide dialogul.
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel", color = Color(0xFFFFE4D6), fontFamily = CherryBomb)
                    }
                    },
                    title = {
                        Text(
                            "New Study Session",
                            fontFamily = CherryBomb,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFE4D6)
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xCCEDC4B3),
                                            Color(0xCCD69F7E),
                                            Color(0xCC9D6B53)
                                        )
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(20.dp)
                        ) {
                            //câmp pentru numele sesiunii.
                            OutlinedTextField(
                                value = newSessionName,
                                onValueChange = { newSessionName = it },
                                label = { Text("Session Name", color = Color(0xFFFFE4D6)) },
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(  // AICI e corect în Material3
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    disabledTextColor = Color.White,
                                    cursorColor = Color(0xFFFFE4D6),

                                    focusedContainerColor = Color(0x33FFFFFF),  // fundal semi-transparent
                                    unfocusedContainerColor = Color(0x22FFFFFF),
                                    disabledContainerColor = Color(0x22FFFFFF),

                                    focusedIndicatorColor = Color(0xFFFFE4D6),   // linia de jos când e focus
                                    unfocusedIndicatorColor = Color(0x99FFE4D6),

                                    focusedLabelColor = Color(0xFFFFE4D6),
                                    unfocusedLabelColor = Color(0xCCFFE4D6),

                                    errorCursorColor = Color.Red,
                                    errorIndicatorColor = Color.Red
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                "Session Type:",
                                color = Color(0xFFFFE4D6),
                                fontFamily = CherryBomb,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                //selectezi tipul sesiunii: Timer Fix sau Cronometru Liber.
                                RadioButton(
                                    selected = newSessionType == "Timer Fix",
                                    onClick = { newSessionType = "Timer Fix" },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFFFFE4D6),
                                        unselectedColor = Color(0x99FFFFFF)
                                    )
                                )
                                Text(
                                    "Timer Fix",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            // Cronometru Liber
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = newSessionType == "Cronometru Liber",
                                    onClick = { newSessionType = "Cronometru Liber" },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFFFFE4D6),
                                        unselectedColor = Color(0x99FFFFFF)
                                    )
                                )
                                Text(
                                    "Cronometru Liber",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    },
                    containerColor = Color.Transparent, // face fundalul dialogului transparent ca să se vadă gradientul mare
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF702E1F).copy(alpha = 0.95f),
                                    Color(0xFF774936).copy(alpha = 0.98f),
                                    Color(0xFF9D6B53)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(8.dp) // un mic "border" transparent care dă adâncime
                )
            }
        }
    }

