package com.example.taskly.presentacion.ViewModel

import com.example.taskly.domain.models.Note
import com.example.taskly.domain.models.Priority
import com.example.taskly.data.repository.NoteRepository
import kotlinx.coroutines.flow.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class NoteFormState(
    val title: String       = "",
    val description: String = "",
    val dueDate: String     = "",
    val priority: Priority  = Priority.MEDIUM,
    val isSaved: Boolean    = false,
)

class ViewmodelCE(
    private val noteRepo: NoteRepository,
) : ViewModel() {

    private val _formState = MutableStateFlow(NoteFormState())
    val formState: StateFlow<NoteFormState> = _formState.asStateFlow()

    /** Call when entering edit mode to pre-fill the form */
    fun loadNote(id: String) {
        viewModelScope.launch {
            noteRepo.getNoteById(id).collect { note ->
                note?.let {
                    _formState.update {
                        it.copy(
                            title = note.title,
                            description = note.description,
                            dueDate = note.expiration_date,
                            priority = Priority.valueOf(note.level_priority.uppercase())
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(v: String)       = _formState.update { it.copy(title = v) }
    fun onDescriptionChange(v: String) = _formState.update { it.copy(description = v) }
    fun onDueDateChange(v: String)     = _formState.update { it.copy(dueDate = v) }
    fun onPriorityChange(p: Priority)  = _formState.update { it.copy(priority = p) }

    fun save(editingId: String? = null) {
        val s = _formState.value
        if (s.title.isBlank()) return

        val note = Note(
            id = editingId ?: "",
            id_user = "",
            title = s.title,
            description = s.description,
            expiration_date = s.dueDate,
            level_priority = s.priority.name,
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