package com.mujer_virtuosa.ui.viewmodel.admin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.admin.AdminProgressResponse
import com.mujer_virtuosa.data.repository.admin.ProgressRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProgressViewModel : ViewModel() {

    private val repository = ProgressRepository()

    private val _data = mutableStateOf<AdminProgressResponse?>(null)
    val data: State<AdminProgressResponse?> = _data

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _search = mutableStateOf("")
    val search: State<String> = _search

    private var currentPage = 1
    private var searchJob: Job? = null

    fun loadProgress(page: Int = 1) {
        currentPage = page
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getProgress(_search.value, page)
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
            loadProgress(page = 1)
        }
    }
}
