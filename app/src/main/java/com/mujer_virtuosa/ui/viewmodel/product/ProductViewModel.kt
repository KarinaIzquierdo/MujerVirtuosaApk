package com.mujer_virtuosa.ui.viewmodel.product

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.product.Product
import com.mujer_virtuosa.data.repository.product.ProductRepository
import kotlinx.coroutines.launch
import java.io.File

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _isSaving = mutableStateOf(false)
    val isSaving: State<Boolean> = _isSaving

    private val _saveError = mutableStateOf<String?>(null)
    val saveError: State<String?> = _saveError

    private val _saveSuccess = mutableStateOf(false)
    val saveSuccess: State<Boolean> = _saveSuccess

    // Producto seleccionado para editar (null = crear nuevo)
    private val _selectedProduct = mutableStateOf<Product?>(null)
    val selectedProduct: State<Product?> = _selectedProduct

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getProducts()
                .onSuccess { response ->
                    _products.value = response.data ?: emptyList()
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun createProduct(
        name: String,
        price: String,
        stock: String,
        description: String,
        category: String,
        imageFile: File?
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            _saveError.value = null
            _saveSuccess.value = false

            repository.createProduct(name, price, stock, description, category, imageFile)
                .onSuccess {
                    _saveSuccess.value = true
                    loadProducts()
                }
                .onFailure { exception ->
                    _saveError.value = exception.message
                }

            _isSaving.value = false
        }
    }

    fun updateProduct(
        id: Int,
        name: String,
        price: String,
        stock: String,
        description: String,
        category: String,
        active: Boolean,
        imageFile: File?
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            _saveError.value = null
            _saveSuccess.value = false

            repository.updateProduct(id, name, price, stock, description, category, active, imageFile)
                .onSuccess {
                    _saveSuccess.value = true
                    loadProducts()
                }
                .onFailure { exception ->
                    _saveError.value = exception.message
                }

            _isSaving.value = false
        }
    }

    fun selectProduct(product: Product?) {
        _selectedProduct.value = product
    }

    fun resetSaveState() {
        _saveSuccess.value = false
        _saveError.value = null
    }
}
