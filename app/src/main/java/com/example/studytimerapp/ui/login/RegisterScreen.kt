package com.example.studytimerapp.ui.login


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.studytimerapp.ui.theme.CherryBomb


import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.studytimerapp.data.firebase.FirebaseAuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val authManager = remember { FirebaseAuthManager() }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }


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

    Box(modifier = Modifier.fillMaxSize().background(gradient)) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Join the Café!",
                fontFamily = CherryBomb,
                fontSize = 40.sp,
                color = Color(0xFFFFE4D6),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Începe să studiezi mai cozy",
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
                    // EMAIL
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = Color(0xFFFFE4D6)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
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

                    // PAROLA
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
                                    contentDescription = null,
                                    tint = Color(0xFFFFE4D6)
                                )
                            }
                        },
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
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

                    // CONFIRMĂ PAROLA
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmă parola", color = Color(0xFFFFE4D6)) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
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
                    val authManager = remember { FirebaseAuthManager() }
                    var errorMessage by remember { mutableStateOf<String?>(null) }
                    var loading by remember { mutableStateOf(false) }
                    // BUTON REGISTER
                    Button(
                        onClick = {
                            if (password != confirmPassword) {
                                errorMessage = "Parolele nu coincid"
                                return@Button
                            }
                            loading = true
                            errorMessage = null

                            scope.launch {
                                val result = authManager.register(email, password)
                                loading = false

                                result
                                    .onSuccess {
                                        navController.navigate("home") {
                                            popUpTo("register") { inclusive = true }
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                    .onFailure { e ->
                                        errorMessage = e.message ?: "Înregistrare eșuată"
                                    }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC38E70)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            if (loading) "Se creează contul..." else "Create Account",
                            fontFamily = CherryBomb,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    }

                    // Mesaj de eroare
                    errorMessage?.let { msg ->
                        Text(
                            text = msg,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    // Link înapoi la Login
                    TextButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Ai deja cont? Loghează-te",
                            color = Color(0xFFFFE4D6),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}