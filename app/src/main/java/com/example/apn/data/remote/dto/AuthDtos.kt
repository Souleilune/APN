package com.example.apn.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    @SerializedName("full_name")
    val fullName: String? = null
)

data class AuthResponse(
    val success: Boolean,
    val message: String? = null,
    val user: UserDto? = null,
    val token: String? = null,
    @SerializedName("access_token")
    val accessToken: String? = null
)

data class UserDto(
    val id: String,
    @SerializedName("auth_id")
    val authId: String? = null,
    val email: String,
    @SerializedName("full_name")
    val fullName: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
)