package com.example.emergencytranslator.ui.screens.translator

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergencytranslator.R
import com.example.emergencytranslator.data.core.translation.MLTranslator
import com.example.emergencytranslator.data.core.tts.TTSModule
import com.example.emergencytranslator.data.core.stt.VoiceToTextRecognizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslatorViewModel @Inject constructor(
    private val mlTranslator: MLTranslator, private val voiceToTextRecognizer: VoiceToTextRecognizer, private val ttsModule: TTSModule
) : ViewModel() {
    private val _uiState = MutableStateFlow(TranslatorUiState())
    val uiState: StateFlow<TranslatorUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            initModules()

            voiceToTextRecognizer.state.collect { state ->
                if (state.spokenText.isNotEmpty()) {
                    setInputText(state.spokenText)
                    startTranslate()
                }
                _uiState.update {
                    it.copy(
                        isListening = state.isListening,
                        hasError = if (state.error != null) Error.Transcribing else it.hasError
                    )
                }
            }
        }
    }

    private suspend fun initModules() {
        setIsDownloading(true)
        initTranslator()
        setIsDownloading(false)
    }

    private suspend fun initTranslator() {
        val result = mlTranslator.checkIfModelIsDownloaded()
        if (!result) {
            setHasError(Error.Download)
        }
    }

    fun setInputText(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun setCanRecord(value: Boolean) {
        _uiState.update {
            it.copy(
                canRecord = value, hasError = if (!value) Error.AudioPermission else it.hasError
            )
        }
    }

    fun startListening() {
        voiceToTextRecognizer.startListening()
    }

    fun stopListening() {
        voiceToTextRecognizer.stopListening()
    }

    private fun setOutputText(text: String) {
        _uiState.update { it.copy(outputText = text) }
    }

    private fun setIsDownloading(value: Boolean) {
        _uiState.update { it.copy(isDownloading = value) }
    }

    private fun setHasError(error: Error) {
        _uiState.update { it.copy(hasError = error) }
    }

    private fun setIsTranslating(value: Boolean) {
        _uiState.update { it.copy(isTranslating = value) }
    }

    fun startTranslate() {
        val input = _uiState.value.inputText
        if (input.isEmpty()) {
            return
        }
        viewModelScope.launch {
            setIsTranslating(true)
            val result = mlTranslator.translate(input)
            setIsTranslating(false)
            if (result.isNotEmpty()) {
                setOutputText(result)
            } else {
                setHasError(Error.Translating)
            }
        }
    }

    fun startSpeaking() {
        ttsModule.speak(_uiState.value.outputText)
    }

    fun resetError() {
        _uiState.update { it.copy(hasError = null) }
    }

    override fun onCleared() {
        super.onCleared()
        mlTranslator.close()
        voiceToTextRecognizer.destroy()
        ttsModule.onDestroy()
    }

    enum class Error(@StringRes val message: Int) {
        General(R.string.error_general), Download(R.string.error_download_model), Translating(R.string.error_translating), AudioPermission(
            R.string.error_audio_permission
        ),
        Transcribing(R.string.error_transcribing)
    }
}

data class TranslatorUiState(
    val hasError: TranslatorViewModel.Error? = null,
    val isDownloading: Boolean = false,
    val inputText: String = "",
    val outputText: String = "",

    val canRecord: Boolean = false,
    val isListening: Boolean = false,

    val isTranslating: Boolean = false
)