package com.example.emergencytranslator.ui.screens.settings.download

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.emergencytranslator.R
import com.example.emergencytranslator.ui.FullScreenDialog
import java.util.Locale

@Composable
fun DownloadTranslationDataScreen(
    viewModel: DownloadTranslationDataVieModel,
    popBackStack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    DownloadLanguageDataContent(
        title = R.string.settings_download_translation,
        uiState = uiState,
        onItemClick = viewModel::onItemClick,
        popBackStack = popBackStack,
        resetError = viewModel::resetError
    )
}

@Composable
fun DownloadSSTDataScreen(
    viewModel: DownloadSTTDataViewModel,
    popBackStack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    DownloadLanguageDataContent(
        title = R.string.settings_download_stt,
        uiState = uiState,
        onItemClick = viewModel::onItemClick,
        popBackStack = popBackStack,
        resetError = viewModel::resetError
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun DownloadLanguageDataContent(
    @StringRes title: Int,
    uiState: DownloadLanguageDataUiState,
    onItemClick: (Locale) -> Unit,
    popBackStack: () -> Unit,
    resetError: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    LaunchedEffect(uiState.hasError) {
        if (uiState.hasError) {
            scaffoldState.snackbarHostState.showSnackbar(context.resources.getString(R.string.error_download_model))
            resetError()
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = ""
                            )
                        },
                    )
                },
                title = {
                    Text(text = stringResource(id = title))
                }
            )
        }
    ) {
        LoadingDialog(present = uiState.isDownloading)
        LazyColumn(modifier = Modifier.padding(it)) {
            stickyHeader {
                SectionHeader(title = "Downloaded languages")
            }
            items(uiState.downloadedLanguages) { item ->
                ListItem(
                    text = { Text(text = item.displayLanguage) },
                    trailing = {
                        IconButton(
                            onClick = { onItemClick(item) },
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_download_done_24),
                                    contentDescription = "download done"
                                )
                            },
                        )
                    }
                )
            }
            stickyHeader {
                SectionHeader(title = "Available to download")
            }
            items(uiState.availableLanguages) { item ->
                ListItem(
                    text = { Text(text = item.displayLanguage) },
                    trailing = {
                        IconButton(
                            onClick = { onItemClick(item) },
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_download_24),
                                    contentDescription = "download"
                                )
                            },
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
fun LoadingDialog(present: Boolean) {
    if (present) {
        FullScreenDialog {
            Column {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(100.dp)
                )
                Text(text = "Downloading...")
            }
        }
    }
}