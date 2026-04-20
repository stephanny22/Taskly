package com.example.taskly.presentacion.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskly.presentacion.Components.TasklyBottomNav

@Composable
fun ProfileScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
) {
    Scaffold(
        bottomBar = {
            TasklyBottomNav(
                currentRoute = currentRoute,
                onNavigate   = onNavigate,
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}
