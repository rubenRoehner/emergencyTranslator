package com.example.emergencytranslator.ui.screens.translator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.emergencytranslator.R

@Composable
fun TranslatorScreen(viewModel: TranslatorViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    TranslatorContent(
        uiState = uiState
    )
}

@Composable
private fun TranslatorContent(
    uiState: TranslatorUiState,
) {
    Scaffold(topBar = { AppBar() }) {
        Column(Modifier.padding(it)) {
            Text(text = "TranslatorApp")
        }
    }
}

@Composable
private fun AppBar() {
    TopAppBar {
        Text(text = stringResource(id = R.string.app_name))
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_settings_24),
                contentDescription = "Settings icon"
            )
        }
    }
}