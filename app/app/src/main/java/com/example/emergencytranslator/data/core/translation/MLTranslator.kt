package com.example.emergencytranslator.data.core.translation

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await
import org.tinylog.kotlin.Logger
import java.lang.Exception
import java.util.Locale

@Singleton
class MLTranslator {

    private val translatorOptions = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.GERMAN)
        .setTargetLanguage(TranslateLanguage.ENGLISH)
        .build()

    private val translator = Translation.getClient(this.translatorOptions)

    companion object {
        val availableLanguages = TranslateLanguage.getAllLanguages().map { Locale(it) }
    }

    suspend fun checkIfModelIsDownloaded(options: TranslatorOptions = translatorOptions): Boolean {
        Logger.debug("Checking downloaded")
        val translator = Translation.getClient(options)
        val downloadConditions = DownloadConditions.Builder()
            .build()
        return try {
            Logger.debug("start download if needed")
            translator.downloadModelIfNeeded(downloadConditions).await()
            Logger.debug("done downloading")
            true
        } catch (e: Exception) {
            Logger.error("Error while downloading model", e)
            false
        }
    }

    suspend fun translate(text: String): String {
        Logger.debug("Translating")
        return try {
            translator.translate(text).await()
        } catch (e: Exception) {
            Logger.error("Error while translating", e)
            ""
        }
    }

    fun close() {
        translator.close()
    }
}