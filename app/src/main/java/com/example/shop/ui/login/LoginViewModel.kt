package com.example.shop.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.shop.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState = mutableStateOf(LoginState())
        private set

    fun onChangeEmail(email: String) {
        uiState.value = uiState.value.copy(email = email)
    }

    fun onChangePassword(password: String) {
        uiState.value = uiState.value.copy(password = password)
    }

    fun login(onLoginSuccess: () -> Unit) {
        uiState.value = uiState.value.copy(loading = true, error = null)

        if (uiState.value.email.isEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Email is required",
                loading = false
            )
            return
        }

        if (uiState.value.password.isEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Password is required",
                loading = false
            )
            return
        }

        authRepository.login(
            email = uiState.value.email,
            password = uiState.value.password,
            onSuccess = {
                uiState.value = uiState.value.copy(loading = false)
                onLoginSuccess()
            },
            onError = { errorMessage ->
                uiState.value = uiState.value.copy(
                    error = errorMessage,
                    loading = false
                )
            }
        )
    }
}
