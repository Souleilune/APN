package com.example.apn.domain.model

data class User(
    val id: String = "",
    val authId: String? = null,
    val email: String = "",
    val fullName: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)