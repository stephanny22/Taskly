package com.example.taskly.data.remote

import com.example.taskly.domain.models.Usuario
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseAuthDataSource (
    private val auth: FirebaseAuth
) {

    private val database =
        FirebaseDatabase.getInstance().reference

    suspend fun register(
        nombre: String,
        correo: String,
        password: String
    ) {

        val result = auth
            .createUserWithEmailAndPassword(correo, password)
            .await()

        val uid = result.user?.uid
            ?: throw Exception("No se pudo obtener el UID")

        val usuario = Usuario(
            id = uid,
            name = nombre,
            email = correo
        )

        database
            .child("users")
            .child(uid)
            .setValue(usuario)
            .await()

        // evita problemas en login por falta de persistencia
        auth.currentUser?.reload()
    }

    suspend fun login(
        email: String,
        password: String
    ) {
        auth.signInWithEmailAndPassword(
            email,
            password
        ).await()
    }

    suspend fun getUser(
        uid: String
    ): Usuario {
        val snapshot = database.child("users").child(uid).get().await()
        return snapshot.getValue(Usuario::class.java)
            ?: throw Exception("Usuario no encontrado")
    }

    suspend fun updateUser(
        uid: String, name: String, phone: String
    ) {
        val updates = mapOf<String, Any>(
            "name"  to name,
            "phone" to phone
        )
        database.child("users").child(uid).updateChildren(updates).await()
    }

    suspend fun deleteAccount(
        currentPassword: String
    ) {
        val user  = auth.currentUser ?: throw Exception("No hay usuario autenticado")
        val email = user.email ?: throw Exception("No se pudo obtener el email")

        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user.reauthenticateAndRetrieveData(credential).await()

        database.child("users").child(user.uid).removeValue().await()

        user.delete().await()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun logout() {
        auth.signOut()
    }
}