package com.example.taskly.domain.models

data class UserSettings(
    val darkMode: Boolean  = false,
    val language: String   = "es",   // "es" | "en"
)