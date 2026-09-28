package com.mujer_virtuosa.ui.viewmodel.admin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.admin.AdminDevice
import com.mujer_virtuosa.data.model.admin.AdminProfile
import com.mujer_virtuosa.data.model.admin.ChangePasswordRequest
import com.mujer_virtuosa.data.model.admin.UpdateProfileRequest
import com.mujer_virtuosa.data.repository.admin.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    private val _profile = mutableStateOf<AdminProfile?>(null)
    val profile: State<AdminProfile?> = _profile

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    // Estado de las acciones (editar perfil, contraseña, desactivar)
    private val _actionLoading = mutableStateOf(false)
    val actionLoading: State<Boolean> = _actionLoading

    private val _actionMessage = mutableStateOf<String?>(null)
    val actionMessage: State<String?> = _actionMessage

    private val _actionError = mutableStateOf<String?>(null)
    val actionError: State<String?> = _actionError

    // Dispositivos activos
    private val _devices = mutableStateOf<List<AdminDevice>>(emptyList())
    val devices: State<List<AdminDevice>> = _devices

    private val _devicesLoading = mutableStateOf(false)
    val devicesLoading: State<Boolean> = _devicesLoading

    // Se dispara cuando la cuenta fue desactivada (para cerrar sesión)
    private val _accountDeactivated = mutableStateOf(false)
    val accountDeactivated: State<Boolean> = _accountDeactivated

    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getAdminProfile()
                .onSuccess { response ->
                    _profile.value = response.data
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun updateProfile(name: String, email: String, phone: String) {
        viewModelScope.launch {
            _actionLoading.value = true
            _actionError.value = null
            _actionMessage.value = null

            repository.updateProfile(UpdateProfileRequest(name, email, phone.ifBlank { null }))
                .onSuccess { response ->
                    _profile.value = response.data
                    _actionMessage.value = response.message ?: "Perfil actualizado correctamente."
                }
                .onFailure { exception ->
                    _actionError.value = exception.message
                }

            _actionLoading.value = false
        }
    }

    fun changePassword(current: String, new: String, confirmation: String) {
        viewModelScope.launch {
            _actionLoading.value = true
            _actionError.value = null
            _actionMessage.value = null

            repository.changePassword(ChangePasswordRequest(current, new, confirmation))
                .onSuccess { response ->
                    _actionMessage.value = response.message ?: "Contraseña actualizada correctamente."
                    loadProfile()
                }
                .onFailure { exception ->
                    _actionError.value = exception.message
                }

            _actionLoading.value = false
        }
    }

    fun loadDevices() {
        viewModelScope.launch {
            _devicesLoading.value = true

            repository.getDevices()
                .onSuccess { response ->
                    _devices.value = response.data ?: emptyList()
                }
                .onFailure { exception ->
                    _actionError.value = exception.message
                }

            _devicesLoading.value = false
        }
    }

    fun deactivateAccount() {
        viewModelScope.launch {
            _actionLoading.value = true
            _actionError.value = null

            repository.deactivateAccount()
                .onSuccess {
                    _accountDeactivated.value = true
                }
                .onFailure { exception ->
                    _actionError.value = exception.message
                }

            _actionLoading.value = false
        }
    }

    fun clearActionState() {
        _actionMessage.value = null
        _actionError.value = null
    }
}
