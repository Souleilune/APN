package com.example.apn.data.repository

import com.example.apn.data.remote.api.ApiService
import com.example.apn.data.remote.api.SupabaseClient
import com.example.apn.data.remote.dto.LoginRequest
import com.example.apn.data.remote.dto.RegisterRequest
import com.example.apn.domain.model.User
import com.example.apn.util.Result
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun login(email: String, password: String): Flow<Result<User>> = flow {
        try {
            emit(Result.Loading)

            // Option 1: Using Supabase directly
            val supabaseUser = SupabaseClient.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            // Convert to domain model
            val user = User(
                id = supabaseUser.id ?: "",
                authId = supabaseUser.id,
                email = supabaseUser.email ?: email
            )

            emit(Result.Success(user))

        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Login failed", e))
        }
    }

    suspend fun loginWithApi(email: String, password: String): Flow<Result<User>> = flow {
        try {
            emit(Result.Loading)

            val request = LoginRequest(email, password)
            val response = apiService.login(request)

            if (response.isSuccessful && response.body()?.success == true) {
                val userDto = response.body()?.user
                if (userDto != null) {
                    val user = User(
                        id = userDto.id,
                        authId = userDto.authId,
                        email = userDto.email,
                        fullName = userDto.fullName,
                        createdAt = userDto.createdAt,
                        updatedAt = userDto.updatedAt
                    )
                    emit(Result.Success(user))
                } else {
                    emit(Result.Error("User data not found"))
                }
            } else {
                val errorMsg = response.body()?.message ?: response.message() ?: "Login failed"
                emit(Result.Error(errorMsg))
            }

        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Network error", e))
        }
    }

    suspend fun register(email: String, password: String, fullName: String? = null): Flow<Result<User>> = flow {
        try {
            emit(Result.Loading)

            // Using Supabase
            val supabaseUser = SupabaseClient.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val user = User(
                id = supabaseUser.id ?: "",
                authId = supabaseUser.id,
                email = supabaseUser.email ?: email,
                fullName = fullName
            )

            emit(Result.Success(user))

        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Registration failed", e))
        }
    }

    suspend fun registerWithApi(email: String, password: String, fullName: String? = null): Flow<Result<User>> = flow {
        try {
            emit(Result.Loading)

            val request = RegisterRequest(email, password, fullName)
            val response = apiService.register(request)

            if (response.isSuccessful && response.body()?.success == true) {
                val userDto = response.body()?.user
                if (userDto != null) {
                    val user = User(
                        id = userDto.id,
                        authId = userDto.authId,
                        email = userDto.email,
                        fullName = userDto.fullName,
                        createdAt = userDto.createdAt,
                        updatedAt = userDto.updatedAt
                    )
                    emit(Result.Success(user))
                } else {
                    emit(Result.Error("User data not found"))
                }
            } else {
                val errorMsg = response.body()?.message ?: response.message() ?: "Registration failed"
                emit(Result.Error(errorMsg))
            }

        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Network error", e))
        }
    }

    suspend fun logout(): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            SupabaseClient.client.auth.signOut()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Logout failed", e))
        }
    }

    fun isUserLoggedIn(): Boolean {
        return try {
            SupabaseClient.client.auth.currentUserOrNull() != null
        } catch (e: Exception) {
            false
        }
    }
}