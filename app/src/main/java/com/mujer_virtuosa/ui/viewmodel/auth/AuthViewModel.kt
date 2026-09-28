package com.mujer_virtuosa.ui.viewmodel.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.auth.User
import com.mujer_virtuosa.data.repository.auth.AuthRepository
import com.mujer_virtuosa.utils.TokenManager
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.login(email, password)
                .onSuccess { response ->
                    response.token?.let { TokenManager.saveToken(it) }
                    response.data?.role?.let { TokenManager.saveUserRole(it) }
                    _user.value = response.data
                    _isLoggedIn.value = response.success && response.token != null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun register(name: String, email: String, password: String, passwordConfirmation: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.register(name, email, password, passwordConfirmation)
                .onSuccess { response ->
                    response.token?.let { TokenManager.saveToken(it) }
                    response.data?.role?.let { TokenManager.saveUserRole(it) }
                    _user.value = response.data
                    _isLoggedIn.value = response.success && response.token != null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    // Restaura la sesión usando el token guardado
    fun loadProfile() {
        if (!TokenManager.hasToken()) return

        viewModelScope.launch {
            repository.getProfile()
                .onSuccess { response ->
                    response.data?.role?.let { TokenManager.saveUserRole(it) }
                    _user.value = response.data
                    _isLoggedIn.value = response.success && response.data != null
                }
                .onFailure {
                    // Token inválido o expirado: limpiar sesión local
                    TokenManager.clearSession()
                    _isLoggedIn.value = false
                }
        }
    }

    fun logout() {
        // Limpieza local inmediata: el ViewModel puede destruirse al navegar
        TokenManager.clearSession()
        _user.value = null
        _isLoggedIn.value = false

        viewModelScope.launch {
            // Revoca el token en el servidor (mejor esfuerzo)
            repository.logout()
        }
    }
}
