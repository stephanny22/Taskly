package com.example.taskly.presentacion.Screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskly.presentacion.ViewModel.ViewModelVerifyCode

// 🎨 Colores
private val OrangePrimary = Color(0xFFFF6B35)
private val TextDark      = Color(0xFF1A1A2E)
private val TextMedium    = Color(0xFF6B7280)
private val DividerColor  = Color(0xFFF0F0F0)
private val ScreenBg      = Color(0xFFF8F9FA)

@Composable
fun VerifyCodeScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: ViewModelVerifyCode = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // 🔔 Mensajes
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            snackbarHostState.showSnackbar("Código verificado")
            viewModel.clearSuccess()
            onSuccess()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Verificación",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingresa el código enviado a tu correo",
                fontSize = 14.sp,
                color = TextMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔢 Input código
            OutlinedTextField(
                value = uiState.code,
                onValueChange = { viewModel.onCodeChange(it) },
                label = { Text("Código de 6 dígitos") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrangePrimary,
                    unfocusedBorderColor = DividerColor,
                    cursorColor = OrangePrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔘 Verificar
            Button(
                onClick = { viewModel.verifyCode() },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Verificar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ⏱️ Contador / Reenviar
            if (!uiState.canResend) {
                Text(
                    text = "Reenviar código en ${uiState.timer}s",
                    color = TextMedium
                )
            } else {
                TextButton(onClick = { viewModel.resendCode() }) {
                    Text("Reenviar código", color = OrangePrimary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔙 Volver
            TextButton(onClick = onBack) {
                Text("Volver", color = TextMedium)
            }
        }
    }
}
