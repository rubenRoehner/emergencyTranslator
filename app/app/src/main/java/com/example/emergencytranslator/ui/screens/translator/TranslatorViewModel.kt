package com.example.emergencytranslator.ui.screens.translator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergencytranslator.data.MLTranslator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tinylog.kotlin.Logger
import javax.inject.Inject

@HiltViewModel
class TranslatorViewModel @Inject constructor(private val mlTranslator: MLTranslator) : ViewModel() {
    private val _uiState = MutableStateFlow(TranslatorUiState())
    val uiState: StateFlow<TranslatorUiState> = _uiState.asStateFlow()

    fun initTranslator() {
        Logger.debug("Init view model")
        viewModelScope.launch {
            setIsDownloading(true)
            val result = mlTranslator.checkIfModelIsDownloaded()
            setIsDownloading(false)
            if(!result) {
                setHasError(Error.Download)
            }
        }
    }

    fun setInputText(text: String) {
        _uiState.update { it.copy(inputText = text) }
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

    fun startTranslate() {
        val input = _uiState.value.inputText
        if (input.isEmpty()){
            return
        }
        Logger.debug("translating $input")
        viewModelScope.launch {
            val result = mlTranslator.translate(input)
            if(result.isNotEmpty()) {
                setOutputText(result)
            } else {
                setHasError(Error.Translating)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mlTranslator.close()
    }

    enum class Error{
        General, Download, Translating
    }
}

data class TranslatorUiState(
    val hasError: TranslatorViewModel.Error? = null,
    val isDownloading: Boolean = false,
    val inputText: String = "",
    val outputText: String = ""
)