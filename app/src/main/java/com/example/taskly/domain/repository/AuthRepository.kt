package com.example.taskly.domain.repository

import com.example.taskly.domain.models.Usuario

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): Result<Unit>

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Unit>

    fun logout()

    fun isUserLoggedIn(): Boolean

    fun getCurrentUserId(): String?

    suspend fun getUser(
        uid: String
    ): Result<Usuario>

    suspend fun updateUser(
        uid: String, name: String, phone: String
    ): Result<Unit>

    suspend fun deleteAccount(
        currentPassword: String
    ): Result<Unit>
}