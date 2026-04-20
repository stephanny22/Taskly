package com.example.taskly.presentacion.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskly.presentacion.Components.NoteCard
import com.example.taskly.presentacion.Config.OrangePrimary
import com.example.taskly.presentacion.Components.TasklyBottomNav
import com.example.taskly.presentacion.ViewModel.ViewModelHome

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ViewModelHome,
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onCreateNote: () -> Unit,
    onEditNote: (String) -> Unit,
    onShareNote: (String) -> Unit,
) {
    val state    by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(bottom = 12.dp),
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "Taskly",
                            color      = OrangePrimary,
                            fontSize   = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                )

                // ── Search bar ────────────────────────────────
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    Icon(Icons.Outlined.Search, "Buscar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(8.dp))
                    BasicTextField(
                        value         = state.searchQuery,
                        onValueChange = viewModel::onSearchChange,
                        singleLine    = true,
                        textStyle     = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        decorationBox = { inner ->
                            if (state.searchQuery.isEmpty()) {
                                Text("Buscar notas...",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium)
                            }
                            inner()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick           = onCreateNote,
                containerColor    = OrangePrimary,
                contentColor      = androidx.compose.ui.graphics.Color.White,
                modifier          = Modifier.padding(bottom = 8.dp),
            ) {
                Icon(Icons.Filled.Add, "Crear nota", modifier = Modifier.size(28.dp))
            }
        },
        bottomBar = {
            TasklyBottomNav(currentRoute = currentRoute, onNavigate = onNavigate)
        },
    ) { innerPadding ->
        if (state.notes.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No hay notas aún", fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("Toca + para crear tu primera nota",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    start  = 16.dp,
                    end    = 16.dp,
                    top    = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 8.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.notes, key = { it.id }) { note ->
                    NoteCard(
                        note             = note,
                        onEdit           = { onEditNote(note.id) },
                        onDelete         = { viewModel.deleteNote(note.id) },
                        onToggleComplete = { viewModel.toggleComplete(note.id) },
                        onShare          = { onShareNote(note.id) },
                    )
                }
            }
        }
    }
}