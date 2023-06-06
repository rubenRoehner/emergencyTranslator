package com.example.emergencytranslator.ui.screens.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.emergencytranslator.R
import com.example.emergencytranslator.data.storage.entities.HistoryItem
import com.example.emergencytranslator.ui.screens.history.components.HistoryHeaderItem
import com.example.emergencytranslator.ui.screens.history.components.HistoryListItem
import java.time.Instant
import java.time.ZoneId

@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    HistoryContent(
        uiState = uiState,
        resetError = viewModel::resetError,
        onExpandItem = viewModel::expandHistoryItem,
        onDeleteButtonClick = viewModel::deleteHistoryItem
    )
}

@Composable
private fun HistoryContent(
    uiState: HistoryUiState,
    resetError: () -> Unit,
    onExpandItem: (HistoryItem) -> Unit,
    onDeleteButtonClick: (HistoryItem) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.hasError) {
        if (uiState.hasError != null) {
            snackbarHostState.showSnackbar(context.resources.getString(uiState.hasError.message))
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
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (uiState.items.isEmpty()) {
                HistoryEmpty()
            } else {
                HistoryList(
                    uiState = uiState,
                    onExpandItem = onExpandItem,
                    onDeleteButtonClick = onDeleteButtonClick
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistoryList(
    uiState: HistoryUiState,
    onExpandItem: (HistoryItem) -> Unit,
    onDeleteButtonClick: (HistoryItem) -> Unit,
) {
    val itemsGrouped = uiState.items.groupBy {
        Instant.ofEpochMilli(it.timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
            .atStartOfDay()
    }
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        itemsGrouped.forEach { (localDateTime, groupItems) ->
            stickyHeader {
                HistoryHeaderItem(localDateTime = localDateTime)
            }
            items(
                items = groupItems.sortedByDescending { it.timestamp },
                key = { item: HistoryItem -> item.id }) {
                HistoryListItem(
                    item = it,
                    onClick = {},
                    expanded = it.id == uiState.expandedHistoryItem,
                    onExpandClick = { onExpandItem(it) },
                    onDeleteButtonClick = { onDeleteButtonClick(it) }
                )
            }
        }
    }

}

@Composable
private fun HistoryEmpty() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.undraw_note_list),
            contentDescription = "History list",
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
                .padding(LocalConfiguration.current.screenWidthDp.dp / 10)
                .size(LocalConfiguration.current.screenWidthDp.dp / 2)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.history_empty),
            style = MaterialTheme.typography.titleMedium
        )
    }
}