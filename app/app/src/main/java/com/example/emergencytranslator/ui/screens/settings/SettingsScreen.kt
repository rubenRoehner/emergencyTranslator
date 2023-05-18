package com.example.emergencytranslator.ui.screens.settings

import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.emergencytranslator.BuildConfig
import com.example.emergencytranslator.R
import com.example.emergencytranslator.ui.screens.settings.components.PreferenceCategory
import com.example.emergencytranslator.ui.screens.settings.components.RegularPreference
import com.example.emergencytranslator.ui.screens.settings.components.SwitchPreference
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateToDownloadSTTData: () -> Unit,
    onNavigateToDownloadTranslateData: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingsContent(
        uiState = uiState,
        setDarkMode = viewModel::setDarkMode,
        onNavigateToDownloadTranslateData = onNavigateToDownloadTranslateData,
        onNavigateToDownloadSTTData = onNavigateToDownloadSTTData
    )
}

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    setDarkMode: (Boolean) -> Unit,
    onNavigateToDownloadSTTData: () -> Unit,
    onNavigateToDownloadTranslateData: () -> Unit
) {
    val context = LocalContext.current
    Column {
        PreferenceCategory(title = "General")
        SwitchPreference(
            title = "dark mode",
            subtitle = "Enable to use dark mode",
            checked = uiState.useDarkMode,
            onCheckedChange = setDarkMode
        )
        Divider()
        PreferenceCategory(title = "Download offline data")
        RegularPreference(
            title = "Translation",
            subtitle = stringResource(id = R.string.settings_download_translation_subtitle),
            onClick = onNavigateToDownloadTranslateData
        )
        RegularPreference(
            title = "Speech to text",
            subtitle = stringResource(id = R.string.settings_download_sst_subtitle),
            onClick = onNavigateToDownloadSTTData
        )
        RegularPreference(title = "Text to speech", subtitle = "", onClick = {
            val intent = Intent()
            intent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
            context.startActivity(intent)
        })
        Divider()
        PreferenceCategory(title = "Info")
        RegularPreference(
            title = "App-version",
            subtitle = BuildConfig.VERSION_NAME,
            onClick = { /*TODO*/ })
        RegularPreference(
            title = "Open source licences",
            subtitle = "List of all used open source licences",
            onClick = {
                context.startActivity(
                    Intent(context, OssLicensesMenuActivity::class.java)
                )
            })
    }
}