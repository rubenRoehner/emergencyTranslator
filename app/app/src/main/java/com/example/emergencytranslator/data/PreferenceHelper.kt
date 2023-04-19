package com.example.emergencytranslator.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences"
val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES)

@Singleton
class DataStoreHelper @Inject constructor(
    @ApplicationContext val context: Context
) {

    private object PreferencesKeys {
        val IS_DARK_THEME = booleanPreferencesKey("pref_dark_theme")
    }

    val isDarkTheme: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] ?: false
        }

    suspend fun clearPreferenceStorage() {
        context.dataStore.edit { mutablePreferences -> mutablePreferences.clear() }
    }

    suspend fun setIsDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferencesKeys.IS_DARK_THEME] = isDarkTheme
        }
    }
}