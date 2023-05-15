package com.example.emergencytranslator.data.core.tts

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Singleton

@Singleton
class TTSModule(context: Context) {

    private val _state = MutableStateFlow(TTSModuleState.Default)

    val state: StateFlow<TTSModuleState>
        get() = _state.asStateFlow()

    private var textToSpeech: TextToSpeech = TextToSpeech(context) {
        if (it == TextToSpeech.SUCCESS) {
            _state.value = TTSModuleState.Initialized
        } else {
            _state.value = TTSModuleState.Error
        }
    }

    fun speak(text: String, language: Locale = Locale.ENGLISH) {
        textToSpeech.language = language
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, "")
    }

    fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    enum class TTSModuleState {
        Default, Initialized, Error
    }
}