package com.example.taskly.domain.usecase

import com.example.taskly.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(
        correo: String,
        password: String
    ) = repository.login(
        correo,
        password
    )
}