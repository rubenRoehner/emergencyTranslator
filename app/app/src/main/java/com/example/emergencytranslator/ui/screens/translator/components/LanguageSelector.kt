package com.example.emergencytranslator.ui.screens.translator.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.emergencytranslator.R
import java.util.Locale

@Composable
fun LanguageSelector(
    availableLanguages: List<Locale>,
    onSourceItemSelected: (Locale) -> Unit,
    onTargetItemSelected: (Locale) -> Unit,
    selectedSource: Locale?,
    selectedTarget: Locale?
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(50),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageDropDown(
                availableLanguages = availableLanguages,
                selectedItem = selectedSource,
                onItemSelected = {
                    onSourceItemSelected(it)
                },
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            LanguageDropDown(
                availableLanguages = availableLanguages.filter { it != selectedSource },
                selectedItem = selectedTarget,
                onItemSelected = { onTargetItemSelected(it) },
            )
        }

    }
}

@Composable
private fun LanguageDropDown(
    availableLanguages: List<Locale>,
    selectedItem: Locale?,
    onItemSelected: (Locale) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Box {
        Row(modifier = Modifier
            .clickable { expanded = true }
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = selectedItem?.displayLanguage ?: stringResource(id = R.string.none),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            availableLanguages.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.displayLanguage
                        )
                    },
                    onClick = {
                        expanded = false
                        onItemSelected(item)
                    },
                )
            }
        }

    }

}