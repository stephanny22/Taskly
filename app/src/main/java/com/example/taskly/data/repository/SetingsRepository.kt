package com.example.taskly.data.repository

import com.example.taskly.data.models.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepository {

    private val _settings = MutableStateFlow(UserSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    fun updateDarkMode(enabled: Boolean) {
        _settings.update { it.copy(darkMode = enabled) }
    }

    fun updateLanguage(lang: String) {
        _settings.update { it.copy(language = lang) }
    }
}