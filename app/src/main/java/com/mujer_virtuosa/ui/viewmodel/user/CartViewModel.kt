package com.mujer_virtuosa.ui.viewmodel.user

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.user.Cart
import com.mujer_virtuosa.data.repository.user.CartRepository
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val repository = CartRepository()

    private val _cart = mutableStateOf<Cart?>(null)
    val cart: State<Cart?> = _cart

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _isAdding = mutableStateOf(false)
    val isAdding: State<Boolean> = _isAdding

    private val _addError = mutableStateOf<String?>(null)
    val addError: State<String?> = _addError

    private val _addSuccess = mutableStateOf(false)
    val addSuccess: State<Boolean> = _addSuccess

    private val _isCheckingOut = mutableStateOf(false)
    val isCheckingOut: State<Boolean> = _isCheckingOut

    private val _checkoutError = mutableStateOf<String?>(null)
    val checkoutError: State<String?> = _checkoutError

    private val _checkoutSuccess = mutableStateOf<String?>(null)
    val checkoutSuccess: State<String?> = _checkoutSuccess

    fun loadCart() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getCart()
                .onSuccess { response ->
                    _cart.value = response.data
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun addToCart(productId: Int, quantity: Int) {
        viewModelScope.launch {
            _isAdding.value = true
            _addError.value = null
            _addSuccess.value = false

            repository.addToCart(productId, quantity)
                .onSuccess { response ->
                    _cart.value = response.data
                    _addSuccess.value = true
                }
                .onFailure { exception ->
                    _addError.value = exception.message
                }

            _isAdding.value = false
        }
    }

    fun updateQuantity(itemId: Int, quantity: Int) {
        viewModelScope.launch {
            repository.updateItem(itemId, quantity)
                .onSuccess { response ->
                    _cart.value = response.data
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
        }
    }

    fun toggleSelected(itemId: Int, quantity: Int, selected: Boolean) {
        viewModelScope.launch {
            repository.updateItem(itemId, quantity, selected)
                .onSuccess { response ->
                    _cart.value = response.data
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
        }
    }

    fun removeItem(itemId: Int) {
        viewModelScope.launch {
            repository.removeItem(itemId)
                .onSuccess {
                    loadCart()
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
        }
    }

    fun checkout() {
        viewModelScope.launch {
            _isCheckingOut.value = true
            _checkoutError.value = null
            _checkoutSuccess.value = null

            repository.checkout()
                .onSuccess { response ->
                    _checkoutSuccess.value = response.message ?: "Compra realizada"
                    loadCart()
                }
                .onFailure { exception ->
                    _checkoutError.value = exception.message
                }

            _isCheckingOut.value = false
        }
    }

    fun resetAddState() {
        _addSuccess.value = false
        _addError.value = null
    }

    fun resetCheckoutState() {
        _checkoutSuccess.value = null
        _checkoutError.value = null
    }
}
