package com.example.emergencytranslator.ui.screens.settings.download

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

abstract class DownloadLanguageDataViewModel: ViewModel() {
    internal val mutableStateFlow = MutableStateFlow(DownloadLanguageDataUiState())
    val uiState: StateFlow<DownloadLanguageDataUiState>
        get() = mutableStateFlow.asStateFlow()

    abstract fun onItemClick(item: Locale)
    fun resetError() {
        mutableStateFlow.update { it.copy(hasError = false)}
    }
}

data class DownloadLanguageDataUiState(
    val availableLanguages: List<Locale> = emptyList(),
    val downloadedLanguages: List<Locale> = emptyList(),
    val isDownloading: Boolean = false,
    val hasError: Boolean = false
)