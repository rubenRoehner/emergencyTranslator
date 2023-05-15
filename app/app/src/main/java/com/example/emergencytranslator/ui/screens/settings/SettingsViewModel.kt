package com.example.emergencytranslator.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergencytranslator.data.core.stt.LanguageDetailsChecker
import com.example.emergencytranslator.data.storage.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val dataStoreHelper: DataStoreHelper): ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    val recognizerDetails = LanguageDetailsChecker()

    init {
        viewModelScope.launch {
            dataStoreHelper.isDarkTheme.collect { value -> _uiState.update { it.copy(useDarkMode = value) } }
        }
    }

    fun setDarkMode(value: Boolean) {
        viewModelScope.launch {
            dataStoreHelper.setIsDarkTheme(value)
        }
    }
}

data class SettingsUiState(
    val useDarkMode: Boolean = false,
)