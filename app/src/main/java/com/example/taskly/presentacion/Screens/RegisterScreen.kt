package com.example.taskly.presentacion.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskly.presentacion.Components.TasklyTextField
import com.example.taskly.presentacion.Config.OrangePrimary
import com.example.taskly.presentacion.Components.TasklyButton
import com.example.taskly.presentacion.ViewModel.ViewModelLR

@Composable
fun RegisterScreen(
    viewModel: ViewModelLR,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val passwordsMatch = state.password == state.confirmPassword
    val isValid = state.name.isNotBlank() && state.email.isNotBlank() &&
            state.password.isNotBlank() && state.confirmPassword.isNotBlank() &&
            passwordsMatch

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onRegisterSuccess()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // ── Logo ──────────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(OrangePrimary),
            ) {
                Icon(Icons.Filled.TaskAlt, "Logo", tint = Color.White, modifier = Modifier.size(44.dp))
            }

            Text("Taskly", fontSize = 28.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface)
            Text("Crea tu cuenta", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            // ── Fields ────────────────────────────────────────
            TasklyTextField(
                value = state.name, onValueChange = viewModel::onNameChange,
                label = "Nombre", placeholder = "Ingresa tu nombre",
            )
            TasklyTextField(
                value = state.email, onValueChange = viewModel::onEmailChange,
                label = "Correo electrónico", placeholder = "Ingresa tu correo",
            )
            TasklyTextField(
                value = state.password, onValueChange = viewModel::onPasswordChange,
                label = "Contraseña", placeholder = "Crea una contraseña", isPassword = true,
            )
            TasklyTextField(
                value = state.confirmPassword, onValueChange = viewModel::onConfirmPasswordChange,
                label = "Confirmar contraseña", placeholder = "Reingresa tu contraseña",
                isPassword = true,
                isError  = state.confirmPassword.isNotEmpty() && !passwordsMatch,
                errorText = "Las contraseñas no coinciden",
            )

            // ── Register button ───────────────────────────────
            TasklyButton(text = "Crear cuenta", onClick = viewModel::register, enabled = isValid)

            // ── Login link ────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Ya tienes una cuenta? ", fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                TextButton(onClick = onNavigateToLogin, contentPadding = PaddingValues(0.dp)) {
                    Text("Iniciar sesión", color = OrangePrimary,
                        fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}