package com.example.taskly.presentacion.Screens.Composables

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
import com.example.taskly.presentacion.ViewModel.ViewmodelCE
import androidx.compose.ui.res.stringResource
import com.example.taskly.R

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
                    Text(
                        text = stringResource(if (editingId != null) R.string.edit_note else R.string.new_note
                    ),
                        fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, stringResource(R.string.back))
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
                label         = stringResource(R.string.note_title),
                placeholder   = stringResource(R.string.note_title_hint),
            )

            TasklyTextField(
                value         = form.description,
                onValueChange = viewModel::onDescriptionChange,
                label         = stringResource(R.string.note_description),
                placeholder   = stringResource(R.string.note_description_hint),
                minLines      = 4,
                maxLines      = 6,
            )

            TasklyTextField(
                value         = form.dueDate,
                onValueChange = viewModel::onDueDateChange,
                label         = stringResource(R.string.note_due_date),
                placeholder   = stringResource(R.string.note_due_date_hint),
            )

            Column {
                Text(
                    stringResource(R.string.note_priority),
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
                text    = stringResource(R.string.save),
                onClick = { viewModel.save(editingId) },
                enabled = form.title.isNotBlank(),
            )
        }
    }
}