package com.example.taskly.presentacion.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskly.data.models.Priority
import com.example.taskly.presentacion.Config.PriorityHigh
import com.example.taskly.presentacion.Config.PriorityLow
import com.example.taskly.presentacion.Config.PriorityMedium

@Composable
fun PriorityChip(priority: Priority, modifier: Modifier = Modifier) {
    val (color, label) = when (priority) {
        Priority.HIGH   -> PriorityHigh to "Alta"
        Priority.MEDIUM -> PriorityMedium to "Media"
        Priority.LOW    -> PriorityLow to "Baja"
    }
    Surface(
        color  = color,
        shape  = RoundedCornerShape(6.dp),
        modifier = modifier,
    ) {
        Text(
            text  = label,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier   = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
        )
    }
}