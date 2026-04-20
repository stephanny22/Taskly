package com.example.taskly.presentacion.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskly.data.models.Note
import com.example.taskly.data.models.UserSettings
import com.example.taskly.data.repository.NoteRepository
import com.example.taskly.data.repository.SettingsRepository
import kotlinx.coroutines.flow.*

data class HomeUiState(
    val notes: List<Note>   = emptyList(),
    val searchQuery: String = "",
)

class ViewModelHome(
    private val noteRepo: NoteRepository,
    private val settingsRepo: SettingsRepository,
) : ViewModel() {

    private val _search = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = combine(
        noteRepo.notes, _search
    ) { notes, query ->
        val filtered = if (query.isBlank()) notes
        else notes.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
        HomeUiState(notes = filtered, searchQuery = query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    val settings: StateFlow<UserSettings> = settingsRepo.settings

    fun onSearchChange(q: String) = _search.update { q }

    fun toggleComplete(id: String) = noteRepo.toggleComplete(id)

    fun deleteNote(id: String) = noteRepo.deleteNote(id)
}