package com.example.taskly.presentacion.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskly.presentacion.Config.OrangeLight
import com.example.taskly.presentacion.Config.OrangePrimary
import com.example.taskly.presentacion.Components.PriorityChip
import com.example.taskly.presentacion.Components.TasklyButton
import com.example.taskly.presentacion.ViewModel.ShareMethod
import com.example.taskly.presentacion.ViewModel.ViewModelSN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareNoteScreen(
    viewModel: ViewModelSN,
    noteId: String,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val note  = remember(noteId) { viewModel.getNoteById(noteId) }

    LaunchedEffect(note) { if (note == null) onBack() }
    LaunchedEffect(state.isShared) { if (state.isShared) onBack() }

    if (note == null) return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compartir nota", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // ── Note preview ──────────────────────────────────
            Card(
                shape     = RoundedCornerShape(12.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PriorityChip(note.priority)
                    Text(note.title, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold)
                    if (note.description.isNotEmpty()) {
                        Text(note.description, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (note.dueDate.isNotEmpty()) {
                        Text("Vence: ${note.dueDate}", style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // ── Share options ─────────────────────────────────
            Card(
                shape     = RoundedCornerShape(12.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Opciones para compartir",
                        style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)

                    listOf(
                        Triple(ShareMethod.TEXT,  Icons.Outlined.Description,
                            "Compartir como texto" to "Copiar al portapapeles o enviar por mensaje"),
                        Triple(ShareMethod.IMAGE, Icons.Outlined.Image,
                            "Compartir como imagen" to "Generar captura de pantalla para compartir"),
                        Triple(ShareMethod.LINK,  Icons.Outlined.Link,
                            "Compartir por enlace" to "Crear enlace para compartir"),
                    ).forEach { (method, icon, labels) ->
                        ShareOptionRow(
                            icon     = icon,
                            title    = labels.first,
                            subtitle = labels.second,
                            selected = state.method == method,
                            onClick  = { viewModel.onMethodChange(method) },
                        )
                    }

                    if (state.method == ShareMethod.LINK) {
                        Surface(
                            color  = OrangeLight,
                            shape  = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column {
                                    Text("Permitir edición",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium)
                                    Text("Otros pueden editar esta nota",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Switch(
                                    checked  = state.allowEditing,
                                    onCheckedChange = viewModel::onAllowEditingChange,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = OrangePrimary,
                                        checkedTrackColor = OrangeLight,
                                    ),
                                )
                            }
                        }
                    }
                }
            }

            TasklyButton(
                text        = "Compartir ahora",
                onClick     = viewModel::share,
                leadingIcon = Icons.Outlined.Share,
            )
        }
    }
}

@Composable
private fun ShareOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (selected) OrangePrimary
    else MaterialTheme.colorScheme.outline
    Surface(
        onClick = onClick,
        shape   = RoundedCornerShape(12.dp),
        color   = if (selected) OrangeLight else Color.Transparent,
        border  = ButtonDefaults.outlinedButtonBorder.copy(width = if (selected) 2.dp else 1.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp),
        ) {
            RadioButton(
                selected = selected,
                onClick  = onClick,
                colors   = RadioButtonDefaults.colors(selectedColor = OrangePrimary),
            )
            Spacer(Modifier.width(8.dp))
            Icon(icon, null, tint = OrangePrimary)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(subtitle, style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}