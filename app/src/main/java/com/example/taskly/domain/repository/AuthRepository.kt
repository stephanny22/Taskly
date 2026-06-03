package com.example.taskly.domain.repository

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

}