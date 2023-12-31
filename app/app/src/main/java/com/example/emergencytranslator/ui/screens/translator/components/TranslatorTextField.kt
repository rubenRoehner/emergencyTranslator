package com.example.emergencytranslator.ui.screens.translator.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
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
    Box(
        modifier = modifier, contentAlignment = Alignment.BottomEnd
    ) {
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
            shape = RoundedCornerShape(6.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                errorContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                disabledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),

                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,

                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,

                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,

                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = MaterialTheme.colorScheme.onBackground,
                errorTextColor = MaterialTheme.colorScheme.onBackground,
            ),
            enabled = !readOnly
        )
        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
        ) {
            Icon(painter = painterResource(id = fabIcon), contentDescription = "")
        }
    }
}