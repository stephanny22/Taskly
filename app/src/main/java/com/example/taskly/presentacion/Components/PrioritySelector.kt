package com.example.taskly.presentacion.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskly.data.models.Priority
import com.example.taskly.presentacion.Config.PriorityHigh
import com.example.taskly.presentacion.Config.PriorityLow
import com.example.taskly.presentacion.Config.PriorityMedium

@Composable
fun PrioritySelector(
    selected: Priority,
    onSelect: (Priority) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier            = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Priority.entries.forEach { priority ->
            val (color, label) = when (priority) {
                Priority.HIGH   -> PriorityHigh to "Alta"
                Priority.MEDIUM -> PriorityMedium to "Media"
                Priority.LOW    -> PriorityLow to "Baja"
            }
            val isSelected = selected == priority
            Surface(
                onClick  = { onSelect(priority) },
                color    = if (isSelected) color else MaterialTheme.colorScheme.surface,
                shape    = RoundedCornerShape(12.dp),
                border   = if (!isSelected) ButtonDefaults.outlinedButtonBorder else null,
                modifier = Modifier.weight(1f).height(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text  = label,
                        color = if (isSelected) Color.White
                        else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}