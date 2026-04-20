package com.example.taskly.presentacion.ViewModel

import androidx.lifecycle.ViewModel
import com.example.taskly.data.models.UserSettings
import com.example.taskly.data.repository.SettingsRepository
import kotlinx.coroutines.flow.*

class ViewModelSettings(
    private val settingsRepo: SettingsRepository,
) : ViewModel() {

    val settings: StateFlow<UserSettings> = settingsRepo.settings

    fun toggleDarkMode()            = settingsRepo.updateDarkMode(!settings.value.darkMode)
    fun setLanguage(lang: String)   = settingsRepo.updateLanguage(lang)
}