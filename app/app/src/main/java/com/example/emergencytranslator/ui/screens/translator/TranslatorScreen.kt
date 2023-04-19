package com.example.emergencytranslator.ui.screens.translator

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun TranslatorScreen(viewModel: TranslatorViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    TranslatorContent(
        uiState = uiState
    )
}

@Composable
private fun TranslatorContent(
    uiState: TranslatorUiState
) {
    Column {
        Text(text = "Translator Screen")
    }
}