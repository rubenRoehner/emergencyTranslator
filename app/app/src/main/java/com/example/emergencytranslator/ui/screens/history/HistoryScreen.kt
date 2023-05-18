package com.example.emergencytranslator.ui.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    HistoryContent(uiState = uiState)
}

@Composable
private fun HistoryContent(uiState: HistoryUiState) {
    Column {
        Text(text = "History Screen")
    }
}