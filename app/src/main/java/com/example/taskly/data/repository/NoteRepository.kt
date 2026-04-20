package com.example.taskly.data.repository

import com.example.taskly.data.models.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class NoteRepository {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    fun addNote(note: Note) {
        _notes.update { it + note.copy(id = UUID.randomUUID().toString()) }
    }

    fun updateNote(id: String, updated: Note) {
        _notes.update { list ->
            list.map { if (it.id == id) updated.copy(id = id) else it }
        }
    }

    fun deleteNote(id: String) {
        _notes.update { list -> list.filter { it.id != id } }
    }

    fun toggleComplete(id: String) {
        _notes.update { list ->
            list.map { if (it.id == id) it.copy(completed = !it.completed) else it }
        }
    }

    fun getNoteById(id: String): Note? = _notes.value.find { it.id == id }
}

