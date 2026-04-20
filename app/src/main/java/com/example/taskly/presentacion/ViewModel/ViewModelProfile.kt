package com.example.taskly.presentacion.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProfileUiState(
    val name: String = "Alexandra López",
    val email: String = "alexandra.lopez@email.com",
    val phone: String = "+57 300 123 4567",
    val isEditingName: Boolean = false,
    val isEditingEmail: Boolean = false,
    val isEditingPhone: Boolean = false,
    val showChangePassword: Boolean = false,
    val showDeleteAccount: Boolean = false,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onPhoneChange(value: String) = _uiState.update { it.copy(phone = value) }

    fun onCurrentPasswordChange(value: String) = _uiState.update { it.copy(currentPassword = value) }
    fun onNewPasswordChange(value: String) = _uiState.update { it.copy(newPassword = value) }
    fun onConfirmPasswordChange(value: String) = _uiState.update { it.copy(confirmPassword = value) }

    fun toggleEditName() = _uiState.update { it.copy(isEditingName = !it.isEditingName) }
    fun toggleEditEmail() = _uiState.update { it.copy(isEditingEmail = !it.isEditingEmail) }
    fun toggleEditPhone() = _uiState.update { it.copy(isEditingPhone = !it.isEditingPhone) }

    fun toggleChangePassword() = _uiState.update {
        it.copy(
            showChangePassword = !it.showChangePassword,
            currentPassword = "",
            newPassword = "",
            confirmPassword = "",
        )
    }

    fun toggleDeleteAccountDialog() = _uiState.update {
        it.copy(showDeleteAccount = !it.showDeleteAccount)
    }

    fun saveProfile() {
        _uiState.update { it.copy(isSaving = true, errorMessage = null) }
        // Simula guardado
        _uiState.update {
            it.copy(
                isSaving = false,
                saveSuccess = true,
                isEditingName = false,
                isEditingEmail = false,
                isEditingPhone = false,
            )
        }
    }

    fun changePassword() {
        val state = _uiState.value
        if (state.newPassword != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden") }
            return
        }
        if (state.newPassword.length < 6) {
            _uiState.update { it.copy(errorMessage = "La contraseña debe tener al menos 6 caracteres") }
            return
        }
        _uiState.update {
            it.copy(
                showChangePassword = false,
                currentPassword = "",
                newPassword = "",
                confirmPassword = "",
                saveSuccess = true,
                errorMessage = null,
            )
        }
    }

    fun deleteAccount() {
        // Aquí iría la lógica real de eliminar cuenta
        _uiState.update { it.copy(showDeleteAccount = false) }
    }

    fun clearSuccess() = _uiState.update { it.copy(saveSuccess = false) }
    fun clearError() = _uiState.update { it.copy(errorMessage = null) }
}