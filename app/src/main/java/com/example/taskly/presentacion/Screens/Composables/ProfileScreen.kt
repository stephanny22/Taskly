package com.example.taskly.presentacion.Screens.Composables

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskly.presentacion.Config.OrangePrimary
import com.example.taskly.presentacion.ViewModel.ProfileViewModel
import androidx.compose.ui.res.stringResource
import com.example.taskly.R

private val OrangeSurface  = Color(0xFFFFF3EE)
private val RedDelete      = Color(0xFFE53935)
private val RedDeleteLight = Color(0xFFFFEBEE)

// ── Screen ───────────────────────────────────────────────────────────────────
@Composable
fun ProfileScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onAccountDeleted: () -> Unit,
    viewModel: ProfileViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val msgSaved = stringResource(R.string.profile_saved)

    LaunchedEffect(uiState.accountDeleted) {
        if (uiState.accountDeleted) onAccountDeleted()
    }

    // Mostrar mensajes
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbarHostState.showSnackbar(msgSaved)
            viewModel.clearSuccess()
        }
    }
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,  // ← era ScreenBg hardcodeado
        snackbarHost   = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            TasklyBottomNav(
                currentRoute = currentRoute,
                onNavigate   = onNavigate,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            // ── Header ──────────────────────────────────────────────────────
            ProfileHeader(name = uiState.name, email = uiState.email)

            Spacer(modifier = Modifier.height(24.dp))

            // ── Sección Información personal ─────────────────────────────
            SectionTitle(title = stringResource(R.string.personal_info))
            Spacer(modifier = Modifier.height(8.dp))

            ProfileCard {
                EditableField(
                    label         = stringResource(R.string.full_name),
                    value         = uiState.name,
                    isEditing     = uiState.isEditingName,
                    icon          = Icons.Outlined.Person,
                    onToggle      = { viewModel.toggleEditName() },
                    onValueChange = { viewModel.onNameChange(it) },
                )
                ProfileDivider()
                EditableField(
                    label         = stringResource(R.string.email),
                    value         = uiState.email,
                    isEditing     = uiState.isEditingEmail,
                    icon          = Icons.Outlined.Email,
                    keyboardType  = KeyboardType.Email,
                    onToggle      = { viewModel.toggleEditEmail() },
                    onValueChange = { viewModel.onEmailChange(it) },
                )
                ProfileDivider()
                EditableField(
                    label         = stringResource(R.string.phone),
                    value         = uiState.phone,
                    isEditing     = uiState.isEditingPhone,
                    icon          = Icons.Outlined.Phone,
                    keyboardType  = KeyboardType.Phone,
                    onToggle      = { viewModel.toggleEditPhone() },
                    onValueChange = { viewModel.onPhoneChange(it) },
                )
            }

            // Botón guardar cambios (aparece si hay algún campo en edición)
            AnimatedVisibility(
                visible = uiState.isEditingName || uiState.isEditingEmail || uiState.isEditingPhone,
                enter   = fadeIn() + expandVertically(),
                exit    = fadeOut() + shrinkVertically(),
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick  = { viewModel.saveProfile() },
                        enabled  = !uiState.isSaving,
                        colors   = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        shape    = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(52.dp),
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                color       = Color.White,
                                modifier    = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Text(
                                text       = stringResource(R.string.save_changes),
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 15.sp,
                                color      = Color.White,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Sección Seguridad ─────────────────────────────────────────
            SectionTitle(title = stringResource(R.string.security))
            Spacer(modifier = Modifier.height(8.dp))

            ProfileCard {
                ActionRow(
                    icon    = Icons.Outlined.Lock,
                    label   = stringResource(R.string.update_password),
                    color   = OrangePrimary,
                    onClick = { viewModel.toggleChangePassword() },
                )
            }

            // Panel de cambio de contraseña
            AnimatedVisibility(
                visible = uiState.showChangePassword,
                enter   = fadeIn() + expandVertically(),
                exit    = fadeOut() + shrinkVertically(),
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    ChangePasswordCard(
                        currentPassword = uiState.currentPassword,
                        newPassword     = uiState.newPassword,
                        confirmPassword = uiState.confirmPassword,
                        onCurrentChange = { viewModel.onCurrentPasswordChange(it) },
                        onNewChange     = { viewModel.onNewPasswordChange(it) },
                        onConfirmChange = { viewModel.onConfirmPasswordChange(it) },
                        onSave          = { viewModel.changePassword() },
                        onCancel        = { viewModel.toggleChangePassword() },
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Sección Cuenta ────────────────────────────────────────────
            SectionTitle(title = stringResource(R.string.account))
            Spacer(modifier = Modifier.height(8.dp))

            ProfileCard {
                ActionRow(
                    icon    = Icons.Outlined.DeleteForever,
                    label   = stringResource(R.string.delete_account),
                    color   = RedDelete,
                    bgColor = RedDeleteLight,
                    onClick = { viewModel.toggleDeleteAccountDialog() },
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // ── Diálogo eliminar cuenta ──────────────────────────────────────────────
    if (uiState.showDeleteAccount) {
        DeleteAccountDialog(
            password        = uiState.currentPassword,
            onPasswordChange = { viewModel.onCurrentPasswordChange(it) },
            onConfirm = { viewModel.deleteAccount() },
            onDismiss = { viewModel.toggleDeleteAccountDialog() },
        )
    }
}

// ── Componentes internos ─────────────────────────────────────────────────────

@Composable
private fun ProfileHeader(name: String, email: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(OrangePrimary)
            .padding(top = 36.dp, bottom = 28.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f))
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text       = name.firstOrNull()?.uppercase() ?: "U",
                    fontSize   = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text       = name,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                color      = Color.White,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text  = email,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.85f),
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text          = title,
        fontSize      = 13.sp,
        fontWeight    = FontWeight.SemiBold,
        color         = MaterialTheme.colorScheme.onSurfaceVariant,  // ← era TextMedium
        modifier      = Modifier.padding(horizontal = 16.dp),
        letterSpacing = 0.5.sp,
    )
}

@Composable
private fun ProfileCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),  // ← era CardBg
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(content = content)
    }
}

@Composable
private fun ProfileDivider() {
    HorizontalDivider(
        modifier  = Modifier.padding(horizontal = 16.dp),
        color     = MaterialTheme.colorScheme.outlineVariant,  // ← era DividerColor
        thickness = 0.8.dp,
    )
}

@Composable
private fun EditableField(
    label: String,
    value: String,
    isEditing: Boolean,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    onToggle: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    // Fondo del ícono: suave naranja en light, naranja translúcido en dark
    val notSpecified = stringResource(R.string.not_specified)
    val isDark = !MaterialTheme.colorScheme.background.isBright()
    val iconBg = if (isDark) OrangePrimary.copy(alpha = 0.15f) else OrangeSurface

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = null,
                tint               = OrangePrimary,
                modifier           = Modifier.size(18.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text     = label,
                fontSize = 11.sp,
                color    = MaterialTheme.colorScheme.onSurfaceVariant,  // ← era TextMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (isEditing) {
                OutlinedTextField(
                    value           = value,
                    onValueChange   = onValueChange,
                    singleLine      = true,
                    textStyle       = LocalTextStyle.current.copy(
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color      = MaterialTheme.colorScheme.onSurface,  // ← era TextDark
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    colors          = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = OrangePrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,  // ← era DividerColor
                        focusedTextColor     = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor   = MaterialTheme.colorScheme.onSurface,
                        cursorColor          = OrangePrimary,
                    ),
                    shape    = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                )
            } else {
                Text(
                    text       = value.ifBlank { notSpecified },
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color      = if (value.isBlank())
                        MaterialTheme.colorScheme.onSurfaceVariant   // ← era TextMedium
                    else
                        MaterialTheme.colorScheme.onSurface,         // ← era TextDark
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onToggle) {
            Icon(
                imageVector        = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                contentDescription = stringResource(if (isEditing) R.string.confirm else R.string.edit),
                tint               = if (isEditing) OrangePrimary
                else MaterialTheme.colorScheme.onSurfaceVariant,  // ← era TextMedium
                modifier           = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun ActionRow(
    icon: ImageVector,
    label: String,
    color: Color,
    bgColor: Color = OrangeSurface,
    onClick: () -> Unit,
) {
    // Igual que EditableField: fondo suave del ícono se adapta al tema
    val isDark        = !MaterialTheme.colorScheme.background.isBright()
    val resolvedBg    = when {
        color == RedDelete -> bgColor                           // rojo siempre igual
        isDark             -> OrangePrimary.copy(alpha = 0.15f) // naranja suave en dark
        else               -> bgColor                           // OrangeSurface en light
    }

    TextButton(
        onClick  = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier          = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(resolvedBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector        = icon,
                    contentDescription = null,
                    tint               = color,
                    modifier           = Modifier.size(18.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text       = label,
                fontSize   = 14.sp,
                fontWeight = FontWeight.Medium,
                color      = color,
                modifier   = Modifier.weight(1f),
            )
            Icon(
                imageVector        = Icons.Default.ChevronRight,
                contentDescription = null,
                tint               = color.copy(alpha = 0.5f),
                modifier           = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun ChangePasswordCard(
    currentPassword: String,
    newPassword: String,
    confirmPassword: String,
    onCurrentChange: (String) -> Unit,
    onNewChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),  // ← era CardBg
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text       = stringResource(R.string.change_password_title),
                fontWeight = FontWeight.SemiBold,
                fontSize   = 15.sp,
                color      = MaterialTheme.colorScheme.onSurface,  // ← era TextDark
            )
            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(
                label         = stringResource(R.string.current_password),
                value         = currentPassword,
                onValueChange = onCurrentChange,
            )
            Spacer(modifier = Modifier.height(12.dp))
            PasswordField(
                label         = stringResource(R.string.new_password),
                value         = newPassword,
                onValueChange = onNewChange,
            )
            Spacer(modifier = Modifier.height(12.dp))
            PasswordField(
                label         = stringResource(R.string.confirm_new_password),
                value         = confirmPassword,
                onValueChange = onConfirmChange,
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedButton(
                    onClick  = onCancel,
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,  // ← era TextMedium
                    ),
                    border   = BorderStroke(
                        1.dp, MaterialTheme.colorScheme.outlineVariant,             // ← era DividerColor
                    ),
                    modifier = Modifier.weight(1f).height(48.dp),
                ) {
                    Text(stringResource(R.string.cancel), fontWeight = FontWeight.Medium)
                }
                Button(
                    onClick  = onSave,
                    colors   = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    shape    = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(48.dp),
                ) {
                    Text(stringResource(R.string.save), color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value                = value,
        onValueChange        = onValueChange,
        label                = { Text(label, fontSize = 12.sp) },
        singleLine           = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector        = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.onSurfaceVariant,  // ← era TextMedium
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = OrangePrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,  // ← era DividerColor
            focusedLabelColor    = OrangePrimary,
            focusedTextColor     = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor   = MaterialTheme.colorScheme.onSurface,
            cursorColor          = OrangePrimary,
        ),
        shape    = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun DeleteAccountDialog(
    password: String,
    onPasswordChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = MaterialTheme.colorScheme.surface,  // ← era CardBg
        shape            = RoundedCornerShape(20.dp),
        icon = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(RedDeleteLight),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector        = Icons.Outlined.DeleteForever,
                    contentDescription = null,
                    tint               = RedDelete,
                    modifier           = Modifier.size(28.dp),
                )
            }
        },
        title = {
            Text(
                text       = stringResource(R.string.delete_account_title),
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onSurface,
                fontSize   = 18.sp,
            )
        },
        text = {
            Text(
                text       = stringResource(R.string.delete_account_msg),
                color      = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize   = 14.sp,
                lineHeight = 20.sp,
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value         = password,
                onValueChange = onPasswordChange,
                label         = { Text(stringResource(R.string.current_password)) },
                singleLine    = true,
                visualTransformation = PasswordVisualTransformation(),
                shape         = RoundedCornerShape(12.dp),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE53935),
                    cursorColor        = Color(0xFFE53935),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            Button(
                onClick  = onConfirm,
                colors   = ButtonDefaults.buttonColors(containerColor = RedDelete),
                shape    = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.delete_confirm), color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick  = onDismiss,
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                border   = BorderStroke(
                    1.dp, MaterialTheme.colorScheme.outlineVariant,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.cancel), fontWeight = FontWeight.Medium)
            }
        },
    )
}

private fun Color.isBright(): Boolean {
    val luminance = 0.2126f * red.coerceIn(0f, 1f) +
            0.7152f * green.coerceIn(0f, 1f) +
            0.0722f * blue.coerceIn(0f, 1f)
    return luminance > 0.5f
}