package com.example.emergencytranslator.ui.screens.translator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun LanguageSelector(
    availableLanguages: List<String>,
    onSourceItemSelected: (String) -> Unit,
    onDestinationItemSelected: (String) -> Unit
) {
    var selectedSourceIndex by remember { mutableStateOf(0) }
    var selectedDestinationIndex by remember { mutableStateOf(1) }
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.primary, RoundedCornerShape(50))
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LanguageDropDown(
            availableLanguages = availableLanguages,
            selectedItem = selectedSourceIndex,
            onItemSelected = { selectedSourceIndex = it },
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "",
            tint = MaterialTheme.colors.secondary
        )
        LanguageDropDown(
            availableLanguages = availableLanguages,
            selectedItem = selectedDestinationIndex,
            onItemSelected = { selectedDestinationIndex = it },
        )
    }
}

@Composable
private fun LanguageDropDown(
    availableLanguages: List<String>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Box {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = availableLanguages[selectedItem],
                style = TextStyle(color = MaterialTheme.colors.onPrimary)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            availableLanguages.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = text
                        )
                    },
                    onClick = {
                        expanded = false
                        onItemSelected(index)
                    },
                )
            }
        }

    }

}