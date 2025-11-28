package com.example.apn.data.remote.api

import com.example.apn.data.remote.dto.AuthResponse
import com.example.apn.data.remote.dto.LoginRequest
import com.example.apn.data.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
}