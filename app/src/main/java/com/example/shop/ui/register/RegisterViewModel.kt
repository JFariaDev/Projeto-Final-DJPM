package com.example.shop.ui.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.shop.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState = mutableStateOf(RegisterState())
        private set

    fun onChangeName(name: String) {
        uiState.value = uiState.value.copy(name = name)
    }

    fun onChangeEmail(email: String) {
        uiState.value = uiState.value.copy(email = email)
    }

    fun onChangePassword(password: String) {
        uiState.value = uiState.value.copy(password = password)
    }

    fun register(onRegisterSuccess: () -> Unit) {
        uiState.value = uiState.value.copy(loading = true, error = null)

        if (uiState.value.name.isEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Name is required",
                loading = false
            )
            return
        }

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

        authRepository.register(
            name = uiState.value.name,
            email = uiState.value.email,
            password = uiState.value.password,
            onSuccess = {
                uiState.value = uiState.value.copy(loading = false)
                onRegisterSuccess()
            },
            onError = {
                uiState.value = uiState.value.copy(
                    error = it,
                    loading = false
                )
            }
        )
    }
}
