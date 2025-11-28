package com.example.apn.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apn.domain.model.User
import com.example.apn.domain.usecase.auth.RegisterUseCase
import com.example.apn.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun register(email: String, password: String, confirmPassword: String, fullName: String? = null) {
        // Validate passwords match
        if (password != confirmPassword) {
            _registerState.value = RegisterState(error = "Passwords do not match")
            return
        }

        viewModelScope.launch {
            registerUseCase(email, password, fullName).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _registerState.value = RegisterState(isLoading = true)
                    }
                    is Result.Success -> {
                        _registerState.value = RegisterState(
                            isLoading = false,
                            user = result.data,
                            isSuccess = true
                        )
                    }
                    is Result.Error -> {
                        _registerState.value = RegisterState(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _registerState.value = _registerState.value.copy(error = null)
    }
}