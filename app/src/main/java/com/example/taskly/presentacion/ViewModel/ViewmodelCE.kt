package com.example.taskly.presentacion.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskly.data.repository.NoteRepository
import com.example.taskly.domain.models.Note
import com.example.taskly.domain.models.Priority
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NoteFormState(
    val title: String = "",
    val description: String = "",
    val dueDate: String = "",
    val priority: Priority = Priority.MEDIUM,
    val isSaved: Boolean = false,
)

class ViewmodelCE(
    private val noteRepo: NoteRepository,
    private val userId: String,
) : ViewModel() {

    private val _formState = MutableStateFlow(NoteFormState())
    val formState: StateFlow<NoteFormState> = _formState.asStateFlow()

    fun loadNote(id: String) {
        viewModelScope.launch {
            noteRepo.getNoteById(id).collect { note ->
                note?.let {
                    _formState.update { state ->
                        state.copy(
                            title = it.title,
                            description = it.description,
                            dueDate = it.expiration_date,
                            priority = runCatching {
                                Priority.valueOf(it.level_priority.uppercase())
                            }.getOrDefault(Priority.MEDIUM)
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(v: String) =
        _formState.update { it.copy(title = v) }

    fun onDescriptionChange(v: String) =
        _formState.update { it.copy(description = v) }

    fun onDueDateChange(v: String) =
        _formState.update { it.copy(dueDate = v) }

    fun onPriorityChange(p: Priority) =
        _formState.update { it.copy(priority = p) }

    fun save(editingId: String? = null) {
        val s = _formState.value
        if (s.title.isBlank()) return

        val note = Note(
            id = editingId ?: "",
            id_user = userId, // 👈 AQUÍ YA VA EL LOGUEADO
            title = s.title,
            description = s.description,
            expiration_date = s.dueDate,
            level_priority = s.priority.name.lowercase(),
            status = "pending"
        )

        if (editingId != null) {
            noteRepo.updateNote(editingId, note)
        } else {
            noteRepo.addNote(note)
        }

        _formState.update { it.copy(isSaved = true) }
    }
}