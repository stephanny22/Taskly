package com.example.taskly.data.repository

import com.example.taskly.data.remote.FirebaseAuthDataSource
import com.example.taskly.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val dataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun register(
        nombre: String,
        correo: String,
        password: String
    ): Result<Unit> {

        return try {

            dataSource.register(
                nombre,
                correo,
                password
            )

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)

        }
    }

    override suspend fun login(
        correo: String,
        password: String
    ): Result<Unit> {

        return try {

            dataSource.login(
                correo,
                password
            )

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)

        }
    }

    override fun getCurrentUserId(): String? {
        return dataSource.getCurrentUserId()
    }

    override fun logout() {
        dataSource.logout()
    }
    override fun isUserLoggedIn(): Boolean {
        return dataSource.getCurrentUserId() != null
    }
}