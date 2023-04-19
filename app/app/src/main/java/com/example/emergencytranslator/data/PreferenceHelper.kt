package com.example.emergencytranslator.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Singleton
class PreferenceHelper(@ApplicationContext context: Context) {
    val prefs = context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

    val useDarkMode = prefs.getBoolean(DARK_MODE_KEY, false)

    fun setDarkMode(boolean: Boolean?) {
    }

    companion object {
        private const val USER_PREFERENCES_NAME = "user_prefs"

        private const val DARK_MODE_KEY = "dark_mode"
    }

}