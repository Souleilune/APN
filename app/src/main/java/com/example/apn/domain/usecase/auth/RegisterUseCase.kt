package com.example.apn.domain.usecase.auth

import com.example.apn.data.repository.AuthRepository
import com.example.apn.domain.model.User
import com.example.apn.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, fullName: String? = null): Flow<Result<User>> {
        // Validate email
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return kotlinx.coroutines.flow.flow {
                emit(Result.Error("Invalid email address"))
            }
        }

        // Validate password
        if (password.isBlank() || password.length < 6) {
            return kotlinx.coroutines.flow.flow {
                emit(Result.Error("Password must be at least 6 characters"))
            }
        }

        return authRepository.register(email, password, fullName)
    }
}