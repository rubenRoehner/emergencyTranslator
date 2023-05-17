package com.example.emergencytranslator.ui.screens.settings.download

import androidx.lifecycle.viewModelScope
import com.example.emergencytranslator.data.core.translation.MLTranslatorService
import com.google.mlkit.nl.translate.TranslateLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DownloadTranslationDataVieModel @Inject constructor() : DownloadLanguageDataViewModel() {
    private val mlTranslatorService = MLTranslatorService()

    init {
        viewModelScope.launch {
            updateList()
        }
    }

    private suspend fun updateList() {
        val downloadedModels = mlTranslatorService.updateDownloadedModels()
        mutableStateFlow.update { state ->
            state.copy(
                availableLanguages = mlTranslatorService.getAvailableLanguages().filter { !downloadedModels.contains(it) },
                downloadedLanguages = downloadedModels
            )
        }

    }

    override fun onItemClick(item: Locale) {
        if (!mutableStateFlow.value.downloadedLanguages.contains(item)) {
            val language = TranslateLanguage.fromLanguageTag(item.language)
            if (language != null) {
                viewModelScope.launch {
                    mutableStateFlow.update { it.copy(isDownloading = true) }
                    val downloaded = mlTranslatorService.downloadLanguageData(language)
                    updateList()
                    mutableStateFlow.update {
                        it.copy(
                            isDownloading = false,
                            hasError = !downloaded
                        )
                    }
                }
            } else {
                mutableStateFlow.update { it.copy(hasError = true) }
            }
        } else {
            mutableStateFlow.update { it.copy(hasError = true) }
        }
    }
}