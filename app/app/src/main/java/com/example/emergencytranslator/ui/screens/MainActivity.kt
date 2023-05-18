package com.example.emergencytranslator.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import com.example.emergencytranslator.data.storage.DataStoreHelper
import com.example.emergencytranslator.ui.theme.EmergencyTranslatorTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreHelper: DataStoreHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val useDarkTheme = dataStoreHelper.useDarkTheme.collectAsState(true)
            EmergencyTranslatorTheme(useDarkTheme = useDarkTheme.value) {
                MainScreen()
            }
        }
    }
}