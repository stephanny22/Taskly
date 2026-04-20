package com.example.taskly.presentacion.Config

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── Brand Colors ──────────────────────────────────────────────
val OrangePrimary   = Color(0xFFFF6B35)
val OrangeDark      = Color(0xFFE85A2A)
val OrangeLight     = Color(0x4DFF6B35)   // 30% opacity

val PriorityHigh    = Color(0xFFEF4444)
val PriorityMedium  = Color(0xFFF59E0B)
val PriorityLow     = Color(0xFF10B981)

// ── Light Palette ─────────────────────────────────────────────
val LightBackground = Color(0xFFF5F7FA)
val LightSurface    = Color(0xFFFFFFFF)
val LightOnSurface  = Color(0xFF1A1A2E)
val LightSecondary  = Color(0xFF666666)

// ── Dark Palette ──────────────────────────────────────────────
val DarkBackground  = Color(0xFF1A1A2E)
val DarkSurface     = Color(0xFF16213E)
val DarkSurface2    = Color(0xFF0F3460)
val DarkOnSurface   = Color(0xFFFFFFFF)
val DarkSecondary   = Color(0xFFCCCCCC)

private val LightColorScheme = lightColorScheme(
    primary          = OrangePrimary,
    onPrimary        = Color.White,
    primaryContainer = OrangeLight,
    background       = LightBackground,
    surface          = LightSurface,
    onSurface        = LightOnSurface,
    onSurfaceVariant = LightSecondary,
    outline          = Color(0xFFE0E0E0),
)

private val DarkColorScheme = darkColorScheme(
    primary          = OrangePrimary,
    onPrimary        = Color.White,
    primaryContainer = OrangeLight,
    background       = DarkBackground,
    surface          = DarkSurface,
    onSurface        = DarkOnSurface,
    onSurfaceVariant = DarkSecondary,
    outline          = Color(0xFF2C3E5A),
)

@Composable
fun TasklyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography(),
        content     = content
    )
}