package com.mujer_virtuosa.ui.viewmodel.user

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujer_virtuosa.data.model.user.Progress
import com.mujer_virtuosa.data.repository.user.ProgressRepository
import kotlinx.coroutines.launch

class ProgressViewModel : ViewModel() {

    private val repository = ProgressRepository()

    private val _myProgress = mutableStateOf<List<Progress>>(emptyList())
    val myProgress: State<List<Progress>> = _myProgress

    private val _community = mutableStateOf<List<Progress>>(emptyList())
    val community: State<List<Progress>> = _community

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

    fun loadProgress() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getProgress()
                .onSuccess { response ->
                    _myProgress.value = response.data?.myProgress ?: emptyList()
                    _community.value = response.data?.community ?: emptyList()
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun createProgress(
        context: Context,
        productId: Int,
        description: String,
        beforeImageUri: Uri,
        afterImageUri: Uri
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            _saveError.value = null
            _saveSuccess.value = false

            repository.createProgress(
                context, productId, description, beforeImageUri, afterImageUri
            )
                .onSuccess {
                    _saveSuccess.value = true
                    loadProgress()
                }
                .onFailure { exception ->
                    _saveError.value = exception.message
                }

            _isSaving.value = false
        }
    }

    fun resetSaveState() {
        _saveSuccess.value = false
        _saveError.value = null
    }
}
