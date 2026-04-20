package com.example.taskly.presentacion.Screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskly.presentacion.ViewModel.ViewModelForgotPass

// 🎨 Colores (los mismos tuyos)
private val OrangePrimary = Color(0xFFFF6B35)
private val OrangeSurface = Color(0xFFFFF3EE)
private val TextDark      = Color(0xFF1A1A2E)
private val TextMedium    = Color(0xFF6B7280)
private val DividerColor  = Color(0xFFF0F0F0)
private val ScreenBg      = Color(0xFFF8F9FA)

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    onCodeSent: () -> Unit,
    viewModel: ViewModelForgotPass = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // 🔔 Mensajes
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
            onCodeSent() //
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        containerColor = ScreenBg,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingresa tu correo y te enviaremos un código de verificación",
                fontSize = 14.sp,
                color = TextMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 📧 Campo email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Correo electrónico") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = OrangePrimary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrangePrimary,
                    unfocusedBorderColor = DividerColor,
                    cursorColor = OrangePrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔘 Botón enviar código
            Button(
                onClick = { viewModel.sendCode() },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Enviar código", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔙 Volver
            TextButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al login", color = TextMedium)
            }
        }
    }
}
