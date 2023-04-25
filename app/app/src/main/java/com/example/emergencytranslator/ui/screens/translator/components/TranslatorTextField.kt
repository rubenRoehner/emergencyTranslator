package com.example.emergencytranslator.ui.screens.translator.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import com.example.emergencytranslator.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TranslatorInputTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onDone: (() -> Unit),
    onRecordButtonClick: () -> Unit,
    isListening: Boolean
) {
    TranslatorTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        readOnly = false,
        onDone = onDone,
        trailingIcon = {
            FloatingActionButton(onClick = onRecordButtonClick) {
                AnimatedContent(targetState = isListening) {
                    if (it) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_stop_circle_24),
                            contentDescription = "stop"
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_mic_24),
                            contentDescription = "mic"
                        )
                    }
                }
            }
        },
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
        trailingIcon = {
            FloatingActionButton(onClick = onSpeakButtonClick) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play")
            }
        },
    )
}

@Composable
fun TranslatorTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean,
    onDone: (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Box(modifier = modifier) {
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
            trailingIcon = trailingIcon,
        )
    }
}