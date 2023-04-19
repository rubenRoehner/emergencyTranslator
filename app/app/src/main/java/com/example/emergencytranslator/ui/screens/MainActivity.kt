package com.example.emergencytranslator.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.emergencytranslator.ui.screens.translator.TranslatorScreen
import com.example.emergencytranslator.ui.theme.EmergencyTranslatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmergencyTranslatorTheme {
                // A surface container using the 'background' color from the theme
                TranslatorScreen(viewModel = hiltViewModel())
            }
        }
    }
}