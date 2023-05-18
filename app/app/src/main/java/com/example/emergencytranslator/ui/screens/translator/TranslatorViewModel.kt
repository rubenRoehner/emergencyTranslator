package com.example.emergencytranslator.ui.screens.translator

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergencytranslator.R
import com.example.emergencytranslator.data.core.translation.MLTranslator
import com.example.emergencytranslator.data.core.tts.TTSModule
import com.example.emergencytranslator.data.core.stt.VoiceToTextRecognizer
import com.example.emergencytranslator.data.core.translation.MLTranslatorService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TranslatorViewModel @Inject constructor(
    private val mlTranslator: MLTranslator,
    private val voiceToTextRecognizer: VoiceToTextRecognizer,
    private val ttsModule: TTSModule
) : ViewModel() {
    private val _uiState = MutableStateFlow(TranslatorUiState())
    val uiState: StateFlow<TranslatorUiState> = _uiState.asStateFlow()

    private val mlTranslatorService = MLTranslatorService()

    init {
        viewModelScope.launch {
            initLanguages()
            voiceToTextRecognizer.state.collect { state ->
                if (state.spokenText.isNotEmpty()) {
                    setInputText(state.spokenText)
                    startTranslate()
                }
                _uiState.update { it.copy(isListening = state.isListening) }
                state.error?.run { setHasError(Error.Transcribing) }
            }
        }
    }

    suspend fun initLanguages() {
        val availableLanguages = mlTranslatorService.updateDownloadedModels()
        _uiState.update {
            it.copy(
                availableLanguages = availableLanguages,
                sourceLanguage = availableLanguages.getOrNull(0),
                targetLanguage = availableLanguages.getOrNull(1)
            )
        }
        availableLanguages.getOrNull(0)?.let { onSourceLanguageSelected(it) }
        availableLanguages.getOrNull(1)?.let { onTargetLanguageSelected(it) }
    }

    fun onSourceLanguageSelected(language: Locale) {
        _uiState.update { it.copy(sourceLanguage = language) }
        if (_uiState.value.targetLanguage == language) {
            _uiState.update { it.copy(targetLanguage = null) }
        }
        mlTranslator.updateSourceLanguage(language.language)
        startTranslate()
    }

    fun onTargetLanguageSelected(language: Locale) {
        _uiState.update { it.copy(targetLanguage = language) }
        mlTranslator.updateTargetLanguage(language.language)
        startTranslate()
    }

    fun setInputText(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun setCanRecord(value: Boolean) {
        _uiState.update { it.copy(canRecord = value) }
        if (!value) {
            setHasError(Error.AudioPermission)
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
        General(R.string.error_general), Translating(R.string.error_translating), AudioPermission(
            R.string.error_audio_permission
        ),
        Transcribing(R.string.error_transcribing)
    }
}

data class TranslatorUiState(
    val hasError: TranslatorViewModel.Error? = null,
    val inputText: String = "",
    val outputText: String = "",

    val canRecord: Boolean = false,
    val isListening: Boolean = false,

    val isTranslating: Boolean = false,

    val availableLanguages: List<Locale> = emptyList(),
    val sourceLanguage: Locale? = null,
    val targetLanguage: Locale? = null,
)