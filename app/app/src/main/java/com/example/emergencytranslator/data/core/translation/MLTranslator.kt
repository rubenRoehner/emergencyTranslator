package com.example.emergencytranslator.data.core.translation

import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await
import org.tinylog.kotlin.Logger
import java.lang.Exception

@Singleton
class MLTranslator {

    var sourceLanguage: String? = null
    var targetLanguage: String? = null

    private var translatorOptions: TranslatorOptions? = null

    private var translator: Translator? = null

    fun updateSourceLanguage(language: String) {
        sourceLanguage = language
        updateTranslator()
    }

    fun updateTargetLanguage(language: String) {
        targetLanguage = language
        updateTranslator()
    }

    private fun updateTranslator() {
        translator?.close()
        if (sourceLanguage != null && targetLanguage != null) {
            translatorOptions = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage!!)
                .setTargetLanguage(targetLanguage!!)
                .build()

            translator = translatorOptions?.let { Translation.getClient(it) }
        }
    }

    suspend fun translate(text: String): String {
        Logger.debug("Translating")
        return try {
            translator!!.translate(text).await()
        } catch (e: Exception) {
            Logger.error("Error while translating", e)
            ""
        }
    }

    fun close() {
        translator?.close()
    }
}