package com.example.emergencytranslator.data.storage

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
        val USE_DARK_THEME = booleanPreferencesKey("pref_dark_theme")
    }

    val useDarkTheme: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USE_DARK_THEME] ?: true
        }

    suspend fun clearPreferenceStorage() {
        context.dataStore.edit { mutablePreferences -> mutablePreferences.clear() }
    }

    suspend fun setUseDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferencesKeys.USE_DARK_THEME] = isDarkTheme
        }
    }
}