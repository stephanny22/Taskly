package com.example.taskly.data.models

data class UserSettings(
    val darkMode: Boolean  = false,
    val language: String   = "es",   // "es" | "en"
)