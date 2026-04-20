package com.example.taskly.presentacion.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

data class AuthUiState(
    val email: String         = "",
    val password: String      = "",
    val name: String          = "",
    val confirmPassword: String = "",
    val isLoading: Boolean    = false,
    val error: String?        = null,
    val isLoggedIn: Boolean   = false,
)

class ViewModelLR : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(v: String)           = _uiState.update { it.copy(email = v) }
    fun onPasswordChange(v: String)        = _uiState.update { it.copy(password = v) }
    fun onNameChange(v: String)            = _uiState.update { it.copy(name = v) }
    fun onConfirmPasswordChange(v: String) = _uiState.update { it.copy(confirmPassword = v) }

    fun login() {
        val s = _uiState.value
        if (s.email.isBlank() || s.password.isBlank()) return
        // TODO: replace with real auth call
        _uiState.update { it.copy(isLoggedIn = true) }
    }

    fun register() {
        val s = _uiState.value
        if (s.name.isBlank() || s.email.isBlank() ||
            s.password.isBlank() || s.password != s.confirmPassword) return
        // TODO: replace with real registration call
        _uiState.update { it.copy(isLoggedIn = true) }
    }

    fun resetAuth() = _uiState.update { AuthUiState() }
}