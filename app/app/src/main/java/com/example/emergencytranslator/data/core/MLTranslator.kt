package com.example.emergencytranslator.data.core

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await
import org.tinylog.kotlin.Logger
import java.lang.Exception

@Singleton
class MLTranslator {

    private val translatorOptions = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.GERMAN)
        .setTargetLanguage(TranslateLanguage.ENGLISH)
        .build()

    private val translator = Translation.getClient(this.translatorOptions)

    suspend fun checkIfModelIsDownloaded(): Boolean {
        Logger.debug("Checking downloaded")
        val downloadConditions = DownloadConditions.Builder()
            .build()
        return try {
            translator.downloadModelIfNeeded(downloadConditions).await()
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