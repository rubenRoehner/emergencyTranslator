package com.example.emergencytranslator.ui.screens.translator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.tinylog.kotlin.Logger

@Composable
fun TranslatorScreen(viewModel: TranslatorViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    viewModel.initTranslator()
    TranslatorContent(
        uiState = uiState,
        onInputTextChange = viewModel::setInputText,
        onInputDone = viewModel::startTranslate
    )
}

@Composable
private fun TranslatorContent(
    uiState: TranslatorUiState,
    onInputTextChange: (String) -> Unit,
    onInputDone: () -> Unit
) {
    Scaffold {
        Column(modifier = Modifier.padding(it)){
            LoadingDialog(present = uiState.isDownloading)
            TranslatorTextField(modifier = Modifier.weight(1f), value = uiState.inputText, onValueChange = onInputTextChange, onDone = onInputDone)
            TranslatorTextField(
                modifier = Modifier.weight(1f), value = uiState.outputText, onValueChange = {}, readOnly = true,
            )
        }
    }

}

@Composable
private fun TranslatorTextField(
    modifier: Modifier, value: String, onValueChange: (String) -> Unit, readOnly: Boolean = false, onDone: (() -> Unit)? = null
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            modifier = Modifier.fillMaxSize(),
            singleLine = false,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    Logger.debug("onDone")
                    if (onDone != null) {
                        onDone()
                    }
                }
            )
        )
    }
}

@Composable
private fun LoadingDialog(present: Boolean) {
    if(present) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator()
            }
        }
    }
}