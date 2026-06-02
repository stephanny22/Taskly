package com.example.taskly.presentacion.Screens.Composables

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
import com.example.taskly.presentacion.Config.OrangePrimary
import com.example.taskly.presentacion.ViewModel.ViewModelLR
import androidx.compose.ui.res.stringResource
import com.example.taskly.R

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

            Text(stringResource(R.string.app_name), fontSize = 28.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface)
            Text(stringResource(R.string.create_account_title), style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            // ── Fields ────────────────────────────────────────
            TasklyTextField(
                value = state.name, onValueChange = viewModel::onNameChange,
                label = stringResource(R.string.name), placeholder = stringResource(R.string.name_hint),
            )
            TasklyTextField(
                value = state.email, onValueChange = viewModel::onEmailChange,
                label = stringResource(R.string.email), placeholder = stringResource(R.string.email_hint),
            )
            TasklyTextField(
                value = state.password, onValueChange = viewModel::onPasswordChange,
                label = stringResource(R.string.password), placeholder = stringResource(R.string.password_create_hint), isPassword = true,
            )
            TasklyTextField(
                value = state.confirmPassword, onValueChange = viewModel::onConfirmPasswordChange,
                label = stringResource(R.string.confirm_password), placeholder = stringResource(R.string.confirm_password_hint),
                isPassword = true,
                isError  = state.confirmPassword.isNotEmpty() && !passwordsMatch,
                errorText = stringResource(R.string.passwords_no_match),
            )

            // ── Register button ───────────────────────────────
            TasklyButton(text = stringResource(R.string.create_account), onClick = viewModel::register, enabled = isValid)

            // ── Login link ────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.already_have_account), fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                TextButton(onClick = onNavigateToLogin, contentPadding = PaddingValues(0.dp)) {
                    Text(stringResource(R.string.login), color = OrangePrimary,
                        fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}