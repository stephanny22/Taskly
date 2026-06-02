package com.example.taskly.data.repository

import android.content.Context
import com.example.taskly.domain.models.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("taskly_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(
        UserSettings(
            darkMode = prefs.getBoolean("dark_mode", false),
            language = prefs.getString("language", "es") ?: "es"
        )
    )
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    fun updateDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("dark_mode", enabled).apply()
        _settings.update { it.copy(darkMode = enabled) }
    }

    fun updateLanguage(lang: String) {
        prefs.edit().putString("language", lang).apply()
        _settings.update { it.copy(language = lang) }
    }
}