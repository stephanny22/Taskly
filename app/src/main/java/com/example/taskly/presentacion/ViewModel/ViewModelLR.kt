package com.example.taskly.presentacion.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskly.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

class ViewModelLR(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(v: String) =
        _uiState.update { it.copy(email = v) }

    fun onPasswordChange(v: String) =
        _uiState.update { it.copy(password = v) }

    fun onNameChange(v: String) =
        _uiState.update { it.copy(name = v) }

    fun onConfirmPasswordChange(v: String) =
        _uiState.update { it.copy(confirmPassword = v) }

    fun login() {

        val state = _uiState.value

        if (
            state.email.isBlank() ||
            state.password.isBlank()
        ) return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            val result = authRepository.login(
                state.email,
                state.password
            )

            result.fold(

                onSuccess = {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoggedIn = true
                        )
                    }
                },

                onFailure = { e ->

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            error = e.message
                        )
                    }
                }
            )
        }
    }

    fun register() {

        val state = _uiState.value

        if (
            state.name.isBlank() ||
            state.email.isBlank() ||
            state.password.isBlank() ||
            state.password != state.confirmPassword
        ) return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            val result = authRepository.register(
                state.name,
                state.email,
                state.password
            )

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoggedIn = true
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
            )
        }
    }

    fun resetAuth() {
        _uiState.value = AuthUiState()
    }
}