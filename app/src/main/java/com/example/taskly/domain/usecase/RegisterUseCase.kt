package com.example.taskly.domain.usecase

import com.example.taskly.domain.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(
        nombre: String,
        correo: String,
        password: String
    ) = repository.register(
        nombre,
        correo,
        password
    )
}