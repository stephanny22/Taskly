package com.example.taskly.presentacion.ViewModel

import androidx.lifecycle.ViewModel
import com.example.taskly.domain.models.UserSettings
import com.example.taskly.data.repository.SettingsRepository
import kotlinx.coroutines.flow.*

class ViewModelSettings(
    private val settingsRepo: SettingsRepository,
) : ViewModel() {

    val settings: StateFlow<UserSettings> = settingsRepo.settings

    private val restart_Event = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val restartEvent = restart_Event.asSharedFlow()

    fun toggleDarkMode()            = settingsRepo.updateDarkMode(!settings.value.darkMode)
    fun setLanguage(lang: String) {
        if (settings.value.language == lang) return
        settingsRepo.updateLanguage(lang)
        restart_Event.tryEmit(Unit)
    }
}