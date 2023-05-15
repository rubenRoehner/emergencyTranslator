package com.example.emergencytranslator.data.core.stt

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.tinylog.kotlin.Logger
import javax.inject.Singleton

@Singleton
class VoiceToTextRecognizer(private val application: Application) : RecognitionListener {

    private val _state = MutableStateFlow(VoiceToTextRecognizerState())

    val state: StateFlow<VoiceToTextRecognizerState>
        get() = _state.asStateFlow()

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(application)

    fun startListening(languageCode: String = "de") {
        Logger.debug("Start Listening")
        _state.update { VoiceToTextRecognizerState() }

        if (!SpeechRecognizer.isRecognitionAvailable(application)) {
            _state.update {
                it.copy(
                    error = "Speech recognition is not available"
                )
            }
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }

        recognizer.setRecognitionListener(this)

        recognizer.startListening(intent)

        _state.update {
            it.copy(
                isListening = true
            )
        }

    }

    fun stopListening() {
        Logger.debug("Stop Listening")
        _state.update {
            it.copy(
                isListening = false
            )
        }

        recognizer.stopListening()
    }

    override fun onReadyForSpeech(extras: Bundle?) {
        _state.update {
            it.copy(
                error = null
            )
        }
    }

    override fun onEndOfSpeech() {
        _state.update {
            it.copy(
                isListening = false
            )
        }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        }
        Logger.error("SpeechRecognizer error: $error")
        _state.update {
            it.copy(
                error = "Error: $error"
            )
        }
    }

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { text ->
                Logger.debug("Result: $text")
                _state.update {
                    it.copy(
                        spokenText = text
                    )
                }
            }
    }

    fun destroy() {
        recognizer.destroy()
    }

    override fun onPartialResults(p0: Bundle?) = Unit

    override fun onEvent(p0: Int, p1: Bundle?) = Unit

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(p0: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    data class VoiceToTextRecognizerState(
        val isListening: Boolean = false,
        val spokenText: String = "",
        val error: String? = null
    )
}