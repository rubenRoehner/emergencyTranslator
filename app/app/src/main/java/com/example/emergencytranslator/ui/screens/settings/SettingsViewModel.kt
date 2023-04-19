package com.example.emergencytranslator.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.example.emergencytranslator.data.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val dataStoreHelper: DataStoreHelper): ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
}

data class SettingsUiState(
    val useDarkMode: Boolean? = null
)