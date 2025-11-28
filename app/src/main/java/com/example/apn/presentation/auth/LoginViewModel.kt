package com.example.apn.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apn.domain.model.User
import com.example.apn.domain.usecase.auth.LoginUseCase
import com.example.apn.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _loginState.value = LoginState(isLoading = true)
                    }
                    is Result.Success -> {
                        _loginState.value = LoginState(
                            isLoading = false,
                            user = result.data,
                            isSuccess = true
                        )
                    }
                    is Result.Error -> {
                        _loginState.value = LoginState(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _loginState.value = _loginState.value.copy(error = null)
    }
}