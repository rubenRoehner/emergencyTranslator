package com.example.emergencytranslator.ui.screens.history

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.example.emergencytranslator.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    fun resetError() {
        _uiState.update { it.copy(hasError = null) }
    }

    enum class Error(@StringRes val message: Int){
        General(R.string.error_general)
    }
}

data class HistoryUiState(
    val hasError: HistoryViewModel.Error? = null
)