package com.example.emergencytranslator.ui.screens.translator

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.emergencytranslator.R
import com.example.emergencytranslator.ui.FullScreenDialog
import com.example.emergencytranslator.ui.screens.translator.components.LanguageSelector
import com.example.emergencytranslator.ui.screens.translator.components.ListeningView
import com.example.emergencytranslator.ui.screens.translator.components.TranslatorInputTextField
import com.example.emergencytranslator.ui.screens.translator.components.TranslatorOutputTextField
import java.util.Locale

@Composable
fun TranslatorScreen(viewModel: TranslatorViewModel) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initLanguages()
    }

    TranslatorContent(
        uiState = uiState,
        onInputTextChange = viewModel::setInputText,
        onInputDone = viewModel::startTranslate,
        setCanRecord = viewModel::setCanRecord,
        startListening = viewModel::startListening,
        stopListening = viewModel::stopListening,
        resetError = viewModel::resetError,
        startSpeaking = viewModel::startSpeaking,
        onSourceLanguageSelected = viewModel::onSourceLanguageSelected,
        onTargetLanguageSelected = viewModel::onTargetLanguageSelected
    )
}

@Composable
private fun TranslatorContent(
    uiState: TranslatorUiState,
    onInputTextChange: (String) -> Unit,
    onInputDone: () -> Unit,
    setCanRecord: (Boolean) -> Unit,
    startListening: () -> Unit,
    stopListening: () -> Unit,
    resetError: () -> Unit,
    startSpeaking: () -> Unit,
    onSourceLanguageSelected: (Locale) -> Unit,
    onTargetLanguageSelected: (Locale) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    LaunchedEffect(uiState.hasError) {
        if (uiState.hasError != null) {
            scaffoldState.snackbarHostState.showSnackbar(context.resources.getString(uiState.hasError.message))
            resetError()
        }
    }

    HandlePermissions(setCanRecord = setCanRecord)

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        TranslatingDialog(present = uiState.isTranslating)
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TranslatorInputTextField(
                    modifier = Modifier.weight(1f),
                    value = uiState.inputText,
                    onValueChange = onInputTextChange,
                    onDone = onInputDone,
                    onRecordButtonClick = {
                        if (uiState.canRecord) {
                            if (!uiState.isListening) {
                                startListening()
                            } else {
                                stopListening()
                            }
                        }
                    }
                )
                LanguageSelector(
                    availableLanguages = uiState.availableLanguages,
                    selectedSource = uiState.sourceLanguage,
                    selectedTarget = uiState.targetLanguage,
                    onSourceItemSelected = onSourceLanguageSelected,
                    onTargetItemSelected = onTargetLanguageSelected,
                )
                TranslatorOutputTextField(
                    modifier = Modifier.weight(1f),
                    value = uiState.outputText,
                    onSpeakButtonClick = startSpeaking
                )
            }
            ListeningView(present = uiState.isListening)
        }
    }

}

@Composable
private fun HandlePermissions(setCanRecord: (Boolean) -> Unit) {
    // Creates an permission request
    val recordAudioLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                setCanRecord(isGranted)
            })

    LaunchedEffect(recordAudioLauncher) {
        // Launches the permission request
        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}

@Composable
private fun TranslatingDialog(present: Boolean) {
    if (present) {
        FullScreenDialog {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_translate_24),
                    contentDescription = "translate"
                )
                Text("Translating...")
            }
        }
    }
}