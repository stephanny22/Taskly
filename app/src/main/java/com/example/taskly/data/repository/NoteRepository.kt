package com.example.taskly.data.repository

import com.example.taskly.domain.models.Note
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update

class NoteRepository(
    private val database: DatabaseReference
) {

    private val notesRef = database.child("notes")

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        listenNotes()
    }

    // Escucha cambios en Firebase en tiempo real
    private fun listenNotes() {
        notesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val list = snapshot.children.mapNotNull { snap ->
                    snap.getValue(Note::class.java)?.copy(id = snap.key ?: "")
                }

                _notes.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                // opcional: log
            }
        })
    }

    // CREATE
    fun addNote(note: Note) {
        val id = notesRef.push().key ?: return

        val data = note.copy(id = id)

        notesRef.child(id).setValue(data)
    }

    // UPDATE
    fun updateNote(id: String, note: Note) {
        notesRef.child(id).setValue(note.copy(id = id))
    }

    // DELETE
    fun deleteNote(id: String) {
        notesRef.child(id).removeValue()
    }

    // TOGGLE STATUS
    fun toggleStatus(id: String) {
        val current = _notes.value.find { it.id == id } ?: return

        val newStatus = if (current.status == "completed") "pending" else "completed"

        notesRef.child(id).child("status").setValue(newStatus)
    }

    // 🔍 GET BY ID (Flow-friendly)
    fun getNoteById(id: String): Flow<Note?> = callbackFlow {
        notesRef.child(id).get()
            .addOnSuccessListener {
                val note = it.getValue(Note::class.java)?.copy(id = id)
                trySend(note)
                close()
            }
            .addOnFailureListener {
                trySend(null)
                close()
            }
        awaitClose {}
    }
}