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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskly.presentacion.Config.OrangePrimary
import com.example.taskly.presentacion.ViewModel.ViewModelLR
import com.example.taskly.R


@Composable
fun LoginScreen(
    viewModel: ViewModelLR,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onLoginSuccess()
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
                    .background(OrangePrimary)
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
            ) {
                Icon(
                    Icons.Filled.TaskAlt,
                    contentDescription = "Logo",
                    tint     = Color.White,
                    modifier = Modifier.size(44.dp),
                )
            }

            Text(
                text = stringResource(R.string.app_name),
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(R.string.welcome_back),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            // ── Fields ────────────────────────────────────────
            TasklyTextField(
                value         = state.email,
                onValueChange = viewModel::onEmailChange,
                label         = stringResource(R.string.email),
                placeholder   = stringResource(R.string.email_hint),
            )

            TasklyTextField(
                value         = state.password,
                onValueChange = viewModel::onPasswordChange,
                label         = stringResource(R.string.password),
                placeholder   = stringResource(R.string.password_hint),
                isPassword    = true,
            )

            // ── Forgot password ───────────────────────────────
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                TextButton(
                    onClick = { onNavigateToForgotPassword() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text     = stringResource(R.string.forgot_password),
                        color = OrangePrimary,
                        fontSize = 13.sp
                    )
                }
            }

            // ── Login button ──────────────────────────────────
            TasklyButton(
                text    = stringResource(R.string.login),
                onClick = viewModel::login,
                enabled = state.email.isNotBlank() && state.password.isNotBlank(),
            )

            // ── Divider ───────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    "  o  ",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            // ── Register button ───────────────────────────────
            TasklyButton(
                text     = stringResource(R.string.register),
                onClick  = onNavigateToRegister,
                outlined = true,
            )
        }
    }
}