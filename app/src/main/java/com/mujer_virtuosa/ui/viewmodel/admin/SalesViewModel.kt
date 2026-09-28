package com.mujer_virtuosa.ui.viewmodel.admin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.admin.AdminSales
import com.mujer_virtuosa.data.repository.admin.SalesRepository
import kotlinx.coroutines.launch

class SalesViewModel : ViewModel() {

    private val repository = SalesRepository()

    private val _sales = mutableStateOf<AdminSales?>(null)
    val sales: State<AdminSales?> = _sales

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadSales() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getSales()
                .onSuccess { response ->
                    _sales.value = response.data
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }
}
