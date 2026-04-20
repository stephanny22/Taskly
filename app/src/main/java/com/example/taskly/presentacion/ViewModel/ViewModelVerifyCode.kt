package com.example.taskly.presentacion.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class VerifyCodeUiState(
    val code: String = "",
    val generatedCode: String = "123456", // simulado
    val timer: Int = 30,
    val canResend: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

class ViewModelVerifyCode : ViewModel() {

    private val _uiState = MutableStateFlow(VerifyCodeUiState())
    val uiState: StateFlow<VerifyCodeUiState> = _uiState.asStateFlow()

    init {
        startTimer()
    }

    fun onCodeChange(value: String) {
        if (value.length <= 6) {
            _uiState.update { it.copy(code = value) }
        }
    }

    fun verifyCode() {
        val state = _uiState.value

        if (state.code == state.generatedCode) {
            _uiState.update { it.copy(success = true) }
        } else {
            _uiState.update { it.copy(errorMessage = "Código incorrecto") }
        }
    }

    fun resendCode() {
        _uiState.update {
            it.copy(
                generatedCode = (100000..999999).random().toString(),
                timer = 30,
                canResend = false
            )
        }
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            for (i in 30 downTo 0) {
                delay(1000)
                _uiState.update { it.copy(timer = i) }
            }
            _uiState.update { it.copy(canResend = true) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(success = false) }
    }
}
