package com.mujer_virtuosa.ui.viewmodel.admin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.admin.AdminOrder
import com.mujer_virtuosa.data.repository.admin.SalesRepository
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {

    private val repository = SalesRepository()

    private val _orders = mutableStateOf<List<AdminOrder>>(emptyList())
    val orders: State<List<AdminOrder>> = _orders

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getOrders()
                .onSuccess { response ->
                    _orders.value = response.data ?: emptyList()
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }
}
