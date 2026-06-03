package com.example.taskly.domain.models

import com.google.firebase.database.PropertyName

data class Note(
    val id: String = "",

    @get:PropertyName("id_user")
    @set:PropertyName("id_user")

    var id_user: String = "",
    val title: String = "",
    val description: String = "",
    val expiration_date: String = "",
    val status: String = "",
    val level_priority: String = ""
)