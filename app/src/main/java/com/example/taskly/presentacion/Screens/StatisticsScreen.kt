package com.example.taskly.presentacion.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskly.presentacion.Config.OrangePrimary
import com.example.taskly.presentacion.Config.PriorityLow
import com.example.taskly.presentacion.Config.PriorityMedium
import com.example.taskly.presentacion.Components.TasklyBottomNav
import com.example.taskly.presentacion.ViewModel.ViewModelStatics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: ViewModelStatics,
    currentRoute: String,
    onNavigate: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Estadísticas", color = OrangePrimary,
                        fontSize = 24.sp, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        bottomBar = {
            TasklyBottomNav(currentRoute = currentRoute, onNavigate = onNavigate)
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            listOf(
                Triple("Total de notas",      state.total,     Icons.Outlined.Notes       to OrangePrimary),
                Triple("Completadas",          state.completed, Icons.Outlined.CheckCircle to PriorityLow),
                Triple("Pendientes",           state.pending,   Icons.Outlined.Schedule    to PriorityMedium),
            ).forEach { (title, value, iconColor) ->
                StatCard(title = title, value = value,
                    icon = iconColor.first, iconColor = iconColor.second)
            }

            // ── Progress card ─────────────────────────────────
            Card(
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier  = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Progreso", style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold)
                        Text(
                            "${(state.progress * 100).toInt()}%",
                            color = OrangePrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress     = { state.progress },
                        modifier     = Modifier.fillMaxWidth().height(12.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color        = OrangePrimary,
                        trackColor   = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap    = StrokeCap.Round,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${state.completed} de ${state.total} notas completadas",
                        style  = MaterialTheme.typography.bodySmall,
                        color  = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: Int, icon: ImageVector, iconColor: Color) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier  = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.12f)),
            ) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("$value", fontSize = 30.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}