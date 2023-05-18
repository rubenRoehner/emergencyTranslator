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
import androidx.compose.material3.Text
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
        fabIcon = R.drawable.baseline_mic_24,
        label = "Input"
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
        fabIcon = R.drawable.baseline_play_arrow_24,
        label = "Output"
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
    @DrawableRes fabIcon: Int,
    label: String
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .then(modifier), contentAlignment = Alignment.BottomEnd
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
            label = { Text(text = label) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,

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