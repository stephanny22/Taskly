package com.example.taskly.presentacion.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskly.data.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.*

data class StatisticsUiState(
    val total: Int      = 0,
    val completed: Int  = 0,
    val pending: Int    = 0,
    val progress: Float = 0f,
)

class ViewModelStatics(
    private val noteRepo: NoteRepository,
) : ViewModel() {

    val uiState: StateFlow<StatisticsUiState> = noteRepo.notes.map { notes ->
        val total     = notes.size
        val completed = notes.count { it.completed }
        StatisticsUiState(
            total     = total,
            completed = completed,
            pending   = total - completed,
            progress  = if (total > 0) completed.toFloat() / total else 0f,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StatisticsUiState())
}