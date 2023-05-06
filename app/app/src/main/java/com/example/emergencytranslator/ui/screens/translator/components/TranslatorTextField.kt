package com.example.emergencytranslator.ui.screens.translator.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.emergencytranslator.R

@Composable
fun TranslatorInputTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onDone: (() -> Unit),
    onRecordButtonClick: () -> Unit
) {
    TranslatorTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        readOnly = false,
        onDone = onDone,
        onFabClick = onRecordButtonClick,
        fabIcon = R.drawable.baseline_mic_24
    )
}

@Composable
fun TranslatorOutputTextField(
    modifier: Modifier, value: String, onSpeakButtonClick: () -> Unit
) {
    TranslatorTextField(
        modifier = modifier,
        value = value,
        onValueChange = { },
        readOnly = true,
        onDone = null,
        onFabClick = onSpeakButtonClick,
        fabIcon = R.drawable.baseline_play_arrow_24
    )
}

@Composable
fun TranslatorTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean,
    onDone: (() -> Unit)?,
    onFabClick: () -> Unit,
    @DrawableRes fabIcon: Int
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .then(modifier),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.surface),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                readOnly = readOnly,
                modifier = Modifier.fillMaxSize(),
                singleLine = false,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (onDone != null) {
                        onDone()
                    }
                }),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                shape = RoundedCornerShape(6.dp)
            )
            FloatingActionButton(
                onClick = onFabClick,
                modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
            ) {
                Icon(painter = painterResource(id = fabIcon), contentDescription = "")
            }

        }
    }
}