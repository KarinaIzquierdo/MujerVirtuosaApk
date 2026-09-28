package com.mujer_virtuosa.ui.viewmodel.admin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.admin.AdminStats
import com.mujer_virtuosa.data.repository.admin.DashboardRepository
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val repository = DashboardRepository()

    private val _stats = mutableStateOf<AdminStats?>(null)
    val stats: State<AdminStats?> = _stats

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadStats() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getStats()
                .onSuccess { response ->
                    _stats.value = response.data
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }
}
