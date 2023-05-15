package com.example.emergencytranslator.ui.screens.settings

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.emergencytranslator.BuildConfig
import com.example.emergencytranslator.ui.screens.settings.components.PreferenceCategory
import com.example.emergencytranslator.ui.screens.settings.components.RegularPreference
import com.example.emergencytranslator.ui.screens.settings.components.SwitchPreference
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val intent = Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS)
        intent.setPackage("com.google.android.googlequicksearchbox")
        context.sendOrderedBroadcast(intent, null, viewModel.recognizerDetails, null, Activity.RESULT_OK, null, null)
    }

    SettingsContent(uiState = uiState, setDarkMode = viewModel::setDarkMode)
}

@Composable
private fun SettingsContent(uiState: SettingsUiState, setDarkMode: (Boolean) -> Unit) {
    val context = LocalContext.current
    Column {
        Text(modifier = Modifier.padding(16.dp), text = "Settings", style = MaterialTheme.typography.h5, color = MaterialTheme.colors.primary)
        PreferenceCategory(title = "General")
        SwitchPreference(title = "dark mode", subtitle = "Enable to use dark mode", checked = uiState.useDarkMode, onCheckedChange = setDarkMode)
        Divider()
        PreferenceCategory(title = "Download offline data")
        RegularPreference(title = "Translation", subtitle = "", onClick = { /*TODO*/ })
        RegularPreference(title = "Text to speech", subtitle = "", onClick = { /*TODO*/ })
        RegularPreference(title = "Speech to text", subtitle = "", onClick = { /*TODO*/ })
        Divider()
        PreferenceCategory(title = "Info")
        RegularPreference(title = "App-version", subtitle = BuildConfig.VERSION_NAME, onClick = { /*TODO*/ })
        RegularPreference(title = "Open source licences", subtitle = "List of all used open source licences", onClick = { context.startActivity(
            Intent(context, OssLicensesMenuActivity::class.java)
        ) })
    }
}