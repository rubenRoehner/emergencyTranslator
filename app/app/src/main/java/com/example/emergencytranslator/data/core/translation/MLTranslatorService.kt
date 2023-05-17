package com.example.emergencytranslator.data.core.translation

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import kotlinx.coroutines.tasks.await
import org.tinylog.kotlin.Logger
import java.lang.Exception
import java.util.Locale

class MLTranslatorService {

    fun getAvailableLanguages(): List<Locale> {
        return TranslateLanguage.getAllLanguages().map { Locale(it) }
    }

    suspend fun updateDownloadedModels(): List<Locale> {
        val modelManager = RemoteModelManager.getInstance()
        return try {
            val downloadedModels =
                modelManager.getDownloadedModels(TranslateRemoteModel::class.java).await().toList()
            downloadedModels.map { model -> Locale(model.language) }
        } catch (e: Exception) {
            Logger.error("Error while getting downloaded models", e)
            emptyList()
        }
    }

    suspend fun downloadLanguageData(translateLanguage: String): Boolean {
        val modelManager = RemoteModelManager.getInstance()
        val languageModel = TranslateRemoteModel.Builder(translateLanguage).build()
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
       return try {
            Logger.debug("start download of $translateLanguage")
            modelManager.download(languageModel, conditions).await()
            Logger.debug("done downloading $translateLanguage")
            true
        } catch (e: Exception) {
            Logger.error("Error while downloading model", e)
           false
        }
    }
}