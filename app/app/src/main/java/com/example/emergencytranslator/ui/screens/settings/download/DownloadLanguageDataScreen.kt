package com.example.emergencytranslator.ui.screens.settings.download

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.emergencytranslator.R
import com.example.emergencytranslator.ui.dialogs.LoadingDialog
import java.util.Locale

@Composable
fun DownloadTranslationDataScreen(
    viewModel: DownloadTranslationDataVieModel
) {
    val uiState by viewModel.uiState.collectAsState()
    DownloadLanguageDataContent(
        uiState = uiState,
        onItemClick = viewModel::onItemClick,
        resetError = viewModel::resetError
    )
}

@Composable
fun DownloadSSTDataScreen(
    viewModel: DownloadSTTDataViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    DownloadLanguageDataContent(
        uiState = uiState,
        onItemClick = viewModel::onItemClick,
        resetError = viewModel::resetError
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DownloadLanguageDataContent(
    uiState: DownloadLanguageDataUiState,
    onItemClick: (Locale) -> Unit,
    resetError: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.hasError) {
        if (uiState.hasError) {
            snackbarHostState.showSnackbar(context.resources.getString(R.string.error_download_model))
            resetError()
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        LoadingDialog(present = uiState.isDownloading)
        LazyColumn(modifier = Modifier.padding(it)) {
            stickyHeader {
                SectionHeader(title = "Downloaded languages")
            }
            items(uiState.downloadedLanguages) { item ->
                ListItem(
                    headlineContent = { Text(text = item.displayLanguage) },
                    trailingContent = {
                        IconButton(
                            onClick = { onItemClick(item) },
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_download_done_24),
                                    contentDescription = "download done",
                                    tint = MaterialTheme.colorScheme.secondary
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
                    headlineContent = { Text(text = item.displayLanguage) },
                    trailingContent = {
                        IconButton(
                            onClick = { onItemClick(item) },
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_download_24),
                                    contentDescription = "download",
                                    tint = MaterialTheme.colorScheme.tertiary
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
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(6.dp))
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}