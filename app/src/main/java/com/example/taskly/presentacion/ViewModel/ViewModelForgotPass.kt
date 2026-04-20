package com.example.taskly.presentacion.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ForgotPasswordUiState(
    val email: String = "",
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class ViewModelForgotPass : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun sendCode() {
        val email = _uiState.value.email

        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingresa tu correo") }
            return
        }

        if (!email.contains("@")) {
            _uiState.update { it.copy(errorMessage = "Correo inválido") }
            return
        }

        // 🔥 Simulación envío
        println("Código enviado a: $email")

        _uiState.update {
            it.copy(successMessage = "Código enviado al correo")
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(successMessage = null, errorMessage = null)
        }
    }
}
