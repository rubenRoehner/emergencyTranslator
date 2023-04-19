package com.example.emergencytranslator.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    SettingsContent(uiState = uiState)
}

@Composable
private fun SettingsContent(uiState: SettingsUiState) {
    Column {
        Text(text = "Settings Screen")
    }
}