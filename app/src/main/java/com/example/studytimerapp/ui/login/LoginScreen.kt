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

import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.studytimerapp.data.firebase.FirebaseAuthManager

import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val authManager = remember { FirebaseAuthManager() }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // IMAGINE DE FUNDAL
        Image(
            painter = painterResource(id = R.drawable.homescreen),
            contentDescription = "Login Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay întunecat (ca să se vadă textul)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x22000000)) // negru 53% opac
        )

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
                color = Color(0xFFFAEBE3),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Hai să studiem împreună ♡",
                fontFamily = CherryBomb,
                fontSize = 22.sp,
                color = Color(0xFFFAEBE3)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x45FFFFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = Color(0xFF041E04)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color(0xFF52303C),
                            unfocusedIndicatorColor = Color(0x99FFE4D6),
                            cursorColor = Color(0xFFFFE4D6),
                            focusedLabelColor = Color(0xFF52303C),
                            unfocusedLabelColor = Color(0xCCFFE4D6)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Parolă", color = Color(0xFF041E04)) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Color(0xFFFFFFFF)
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
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
                            loading = true
                            errorMessage = null

                            scope.launch {
                                val result = authManager.login(email, password)
                                loading = false

                                result
                                    .onSuccess {
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                    .onFailure { e ->
                                        errorMessage = e.message ?: "Login eșuat"
                                    }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA9C488)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            if (loading) "Se conectează..." else "Login",
                            fontFamily = CherryBomb,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    }

                    errorMessage?.let { msg ->
                        Text(
                            text = msg,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    TextButton(
                        onClick = { navController.navigate("register") },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Nu ai cont? Înregistrează-te aici ♡",
                            color = Color(0xFF502633),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}