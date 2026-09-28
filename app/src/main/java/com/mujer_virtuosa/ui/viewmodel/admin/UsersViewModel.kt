package com.mujer_virtuosa.ui.viewmodel.admin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.admin.AdminUsers
import com.mujer_virtuosa.data.repository.admin.UsersRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {

    private val repository = UsersRepository()

    private val _data = mutableStateOf<AdminUsers?>(null)
    val data: State<AdminUsers?> = _data

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _search = mutableStateOf("")
    val search: State<String> = _search

    private var currentPage = 1
    private var searchJob: Job? = null

    fun loadUsers(page: Int = 1) {
        currentPage = page
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getUsers(_search.value, page)
                .onSuccess { response ->
                    _data.value = response.data
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun onSearchChange(query: String) {
        _search.value = query
        // Debounce: espera 400ms antes de buscar
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            loadUsers(page = 1)
        }
    }
}
