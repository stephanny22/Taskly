package com.example.taskly.presentacion.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskly.data.repository.NoteRepository
import com.example.taskly.domain.models.Note
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class ShareMethod { TEXT, IMAGE, LINK }

data class ShareUiState(
    val method: ShareMethod  = ShareMethod.TEXT,
    val allowEditing: Boolean = false,
    val isShared: Boolean     = false,
)

class ViewModelSN(
    private val noteRepo: NoteRepository,
) : ViewModel() {
    private val _note = MutableStateFlow<Note?>(null)
    val note = _note.asStateFlow()

    fun loadNote(id: String) {
        viewModelScope.launch {
            noteRepo.getNoteById(id).collect {
                _note.value = it
            }
        }
    }
    private val _uiState = MutableStateFlow(ShareUiState())
    val uiState: StateFlow<ShareUiState> = _uiState.asStateFlow()

    fun getNoteById(id: String) = noteRepo.getNoteById(id)

    fun onMethodChange(m: ShareMethod)      = _uiState.update { it.copy(method = m) }
    fun onAllowEditingChange(v: Boolean)    = _uiState.update { it.copy(allowEditing = v) }

    fun share() {
        // TODO: implement real share intent
        _uiState.update { it.copy(isShared = true) }
    }
}