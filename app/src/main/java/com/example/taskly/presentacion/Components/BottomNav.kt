package com.example.taskly.presentacion.Components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.taskly.presentacion.Config.OrangeLight
import com.example.taskly.presentacion.Config.OrangePrimary

data class NavItem(val label: String, val icon: ImageVector, val route: String)

val bottomNavItems = listOf(
    NavItem("Inicio",      Icons.Outlined.Home,        "home"),
    NavItem("Estadísticas",Icons.Outlined.BarChart,    "statistics"),
    NavItem("Ajustes",     Icons.Outlined.Settings,    "settings"),
    NavItem("Yo", Icons.Outlined.Person, "profile"),
)

@Composable
fun TasklyBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick  = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) OrangePrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (selected) OrangePrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = OrangeLight,
                ),
            )
        }
    }
}