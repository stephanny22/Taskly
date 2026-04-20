package com.example.taskly.presentacion.ViewModel

import com.example.taskly.data.models.Note
import com.example.taskly.data.models.Priority
import com.example.taskly.data.repository.NoteRepository
import kotlinx.coroutines.flow.*
import androidx.lifecycle.ViewModel

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
        noteRepo.getNoteById(id)?.let { note ->
            _formState.update {
                it.copy(
                    title       = note.title,
                    description = note.description,
                    dueDate     = note.dueDate,
                    priority    = note.priority,
                )
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
        if (editingId != null) {
            noteRepo.updateNote(editingId, Note(
                id = editingId, title = s.title, description = s.description,
                dueDate = s.dueDate, priority = s.priority,
            ))
        } else {
            noteRepo.addNote(Note(
                title = s.title, description = s.description,
                dueDate = s.dueDate, priority = s.priority,
            ))
        }
        _formState.update { it.copy(isSaved = true) }
    }
}