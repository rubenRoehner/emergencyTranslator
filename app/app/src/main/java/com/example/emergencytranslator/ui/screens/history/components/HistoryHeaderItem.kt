package com.example.emergencytranslator.ui.screens.history.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun HistoryHeaderItem(localDateTime: LocalDateTime) {
    Surface(Modifier.fillMaxWidth()) {
        Text(
            localDateTime.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                    .withLocale(Locale.getDefault())
            ),
            modifier = Modifier.padding(vertical = 12.dp),
            style = MaterialTheme.typography.titleLarge
        )
    }
}