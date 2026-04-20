package com.example.taskly.data.models

import java.util.UUID

data class Note(
    val id: String       = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val dueDate: String     = "",
    val priority: Priority  = Priority.MEDIUM,
    val completed: Boolean  = false,
)