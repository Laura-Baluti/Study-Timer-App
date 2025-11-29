package com.example.studytimerapp.ui.login
// app/src/main/java/com/example/studytimerapp/ui/auth/LoginScreen.kt

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.studytimerapp.R
import com.example.studytimerapp.ui.theme.CherryBomb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2A0800),
            Color(0xFF774936),
            Color(0xFF9D6B53),
            Color(0xFFB07D62),
            Color(0xFFC38E70),
            Color(0xFFD69F7E),
            Color(0xFFE6B8A2),
            Color(0xFFEDC4B3),
            Color(0xFFFFFFFF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome Back!",
                fontFamily = CherryBomb,
                fontSize = 42.sp,
                color = Color(0xFFFFE4D6),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Hai să studiem împreună ☕",
                fontSize = 18.sp,
                color = Color(0xFFE6B8A2)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x44FFFFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = Color(0xFFFFE4D6)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = TextFieldDefaults.colors(
                            // Parametri de bază – funcționează peste tot!
                            focusedIndicatorColor = Color(0xFFFFE4D6),
                            unfocusedIndicatorColor = Color(0x99FFE4D6),
                            cursorColor = Color(0xFFFFE4D6),
                            focusedLabelColor = Color(0xFFFFE4D6),
                            unfocusedLabelColor = Color(0xCCFFE4D6)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Parolă", color = Color(0xFFFFE4D6)) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Color(0xFFFFE4D6)
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            // Aceiași parametri simpli
                            focusedIndicatorColor = Color(0xFFFFE4D6),
                            unfocusedIndicatorColor = Color(0x99FFE4D6),
                            cursorColor = Color(0xFFFFE4D6),
                            focusedLabelColor = Color(0xFFFFE4D6),
                            unfocusedLabelColor = Color(0xCCFFE4D6)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            // TODO: autentificare (mai târziu cu Room/Firestore)
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC38E70)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Login", fontFamily = CherryBomb, fontSize = 22.sp, color = Color.White)
                    }

                    TextButton(
                        onClick = { navController.navigate("register") },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Nu ai cont? Înregistrează-te aici ♡",
                            color = Color(0xFFFFE4D6),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}