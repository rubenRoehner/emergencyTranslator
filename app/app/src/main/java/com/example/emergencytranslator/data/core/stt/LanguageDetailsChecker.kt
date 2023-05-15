package com.example.emergencytranslator.data.core.stt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import org.tinylog.kotlin.Logger
import java.util.Locale

class LanguageDetailsChecker: BroadcastReceiver() {
    var supportedLanguages: List<String>? = null
    var supportedLanguagesLocale: List<Locale>? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val results = getResultExtras(true)

        if(results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
            supportedLanguages = results.getCharSequenceArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)?.map { it.toString() }
            supportedLanguagesLocale = supportedLanguages?.map { Locale(it) }
        }
        Logger.debug("onReceive: $supportedLanguages")
        Logger.debug("onReceive: locales $supportedLanguagesLocale")
    }
}