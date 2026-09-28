package com.mujer_virtuosa.ui.viewmodel.user

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.user.Address
import com.mujer_virtuosa.data.model.user.ProfileUser
import com.mujer_virtuosa.data.repository.user.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    private val _user = mutableStateOf<ProfileUser?>(null)
    val user: State<ProfileUser?> = _user

    private val _addresses = mutableStateOf<List<Address>>(emptyList())
    val addresses: State<List<Address>> = _addresses

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _isSaving = mutableStateOf(false)
    val isSaving: State<Boolean> = _isSaving

    private val _saveSuccess = mutableStateOf(false)
    val saveSuccess: State<Boolean> = _saveSuccess

    private val _saveError = mutableStateOf<String?>(null)
    val saveError: State<String?> = _saveError

    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getProfile()
                .onSuccess { response ->
                    _user.value = response.data?.user
                    _addresses.value = response.data?.addresses ?: emptyList()
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun updateProfile(name: String, email: String, phone: String?) {
        viewModelScope.launch {
            _isSaving.value = true
            _saveError.value = null
            _saveSuccess.value = false

            repository.updateProfile(name, email, phone)
                .onSuccess { response ->
                    _user.value = response.data
                    _saveSuccess.value = true
                }
                .onFailure { exception ->
                    _saveError.value = exception.message
                }

            _isSaving.value = false
        }
    }

    fun createAddress(address: String, isDefault: Boolean = false) {
        viewModelScope.launch {
            _isSaving.value = true
            _saveError.value = null
            _saveSuccess.value = false

            repository.createAddress(address, isDefault)
                .onSuccess {
                    _saveSuccess.value = true
                    loadProfile()
                }
                .onFailure { exception ->
                    _saveError.value = exception.message
                }

            _isSaving.value = false
        }
    }

    fun updateAddress(addressId: Int, address: String, isDefault: Boolean = false) {
        viewModelScope.launch {
            _isSaving.value = true
            _saveError.value = null
            _saveSuccess.value = false

            repository.updateAddress(addressId, address, isDefault)
                .onSuccess {
                    _saveSuccess.value = true
                    loadProfile()
                }
                .onFailure { exception ->
                    _saveError.value = exception.message
                }

            _isSaving.value = false
        }
    }

    fun deleteAddress(addressId: Int) {
        viewModelScope.launch {
            repository.deleteAddress(addressId)
                .onSuccess { loadProfile() }
                .onFailure { exception ->
                    _saveError.value = exception.message
                }
        }
    }

    fun resetSaveState() {
        _saveSuccess.value = false
        _saveError.value = null
    }
}
