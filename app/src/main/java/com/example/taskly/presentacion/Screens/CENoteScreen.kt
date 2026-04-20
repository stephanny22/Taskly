package com.example.taskly.presentacion.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskly.presentacion.Components.TasklyTextField
import com.example.taskly.presentacion.Components.PrioritySelector
import com.example.taskly.presentacion.Components.TasklyButton
import com.example.taskly.presentacion.ViewModel.ViewmodelCE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditNoteScreen(
    viewModel: ViewmodelCE,
    editingId: String?,
    onBack: () -> Unit,
) {
    val form by viewModel.formState.collectAsState()

    LaunchedEffect(editingId) {
        editingId?.let { viewModel.loadNote(it) }
    }

    LaunchedEffect(form.isSaved) {
        if (form.isSaved) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (editingId != null) "Editar nota" else "Nueva nota",
                        fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            TasklyTextField(
                value         = form.title,
                onValueChange = viewModel::onTitleChange,
                label         = "Título",
                placeholder   = "Ingresa el título",
            )

            TasklyTextField(
                value         = form.description,
                onValueChange = viewModel::onDescriptionChange,
                label         = "Descripción",
                placeholder   = "Ingresa la descripción",
                minLines      = 4,
                maxLines      = 6,
            )

            TasklyTextField(
                value         = form.dueDate,
                onValueChange = viewModel::onDueDateChange,
                label         = "Fecha de vencimiento",
                placeholder   = "yyyy-mm-dd HH:mm",
            )

            Column {
                Text(
                    "Prioridad",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                PrioritySelector(
                    selected = form.priority,
                    onSelect = viewModel::onPriorityChange,
                )
            }

            Spacer(Modifier.height(4.dp))

            TasklyButton(
                text    = "Guardar",
                onClick = { viewModel.save(editingId) },
                enabled = form.title.isNotBlank(),
            )
        }
    }
}