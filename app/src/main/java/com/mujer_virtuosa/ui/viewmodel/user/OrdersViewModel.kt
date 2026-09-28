package com.mujer_virtuosa.ui.viewmodel.user

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.user.Order
import com.mujer_virtuosa.data.repository.user.OrderRepository
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {

    private val repository = OrderRepository()

    private val _orders = mutableStateOf<List<Order>>(emptyList())
    val orders: State<List<Order>> = _orders

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    val activeOrders: List<Order>
        get() = _orders.value.filter { it.status in ACTIVE_STATUSES }

    val pastOrders: List<Order>
        get() = _orders.value.filter { it.status !in ACTIVE_STATUSES }

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

    companion object {
        private val ACTIVE_STATUSES = setOf("pending", "processing", "shipped")
    }
}
