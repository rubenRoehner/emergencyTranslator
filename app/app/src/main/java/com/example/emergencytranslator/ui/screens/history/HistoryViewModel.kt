package com.example.emergencytranslator.ui.screens.history

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergencytranslator.R
import com.example.emergencytranslator.data.storage.daos.HistoryItemDao
import com.example.emergencytranslator.data.storage.entities.HistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tinylog.kotlin.Logger
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val historyItemDao: HistoryItemDao) :
    ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                historyItemDao.getAllItems().collect { items ->
                    _uiState.update { it.copy(items = items) }
                }
            } catch (e: Exception) {
                Logger.error("Error while fetching history items", e)
                setHasError(Error.General)
            }
        }
    }

    private fun setHasError(error: Error) {
        _uiState.update { it.copy(hasError = error) }
    }

    fun resetError() {
        _uiState.update { it.copy(hasError = null) }
    }

    fun expandHistoryItem(item: HistoryItem) {
        if (_uiState.value.expandedHistoryItem != item.id) {
            _uiState.update { it.copy(expandedHistoryItem = item.id) }
        } else {
            _uiState.update { it.copy(expandedHistoryItem = null) }
        }

    }

    fun deleteHistoryItem(item: HistoryItem) {
        try {
            viewModelScope.launch { historyItemDao.delete(item) }
        } catch (e: Exception) {
            Logger.error("Error while deleting HistoryItem", e)
            setHasError(error = Error.Deleting)
        }
    }

    enum class Error(@StringRes val message: Int) {
        General(R.string.error_general),
        Deleting(R.string.error_deleting)
    }
}

data class HistoryUiState(
    val hasError: HistoryViewModel.Error? = null,
    val items: List<HistoryItem> = emptyList(),
    val expandedHistoryItem: Int? = null
)