package com.example.cubetime.ui.settings

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.dataStore:DataStore<Preferences>
                by preferencesDataStore("datastore")


class SettingsDataManager(val context: Context) {
    private val themeKey = booleanPreferencesKey("ThemeApp")
    private val languageKey = stringPreferencesKey("LanguageApp")



    suspend fun  saveSettings(settingsData: SettingsData){
        context.dataStore.edit { pref->
            pref[themeKey] = settingsData.isDarkMode
            pref[languageKey] = settingsData.language
        }
    }


    fun getTheme(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    fun getLanguage(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[languageKey] ?: "ru"
        }
    }

    fun getSettings(): Flow<SettingsData> {
        return context.dataStore.data.map { preferences ->
            SettingsData(
                isDarkMode = preferences[themeKey] ?: false,
                language = preferences[languageKey] ?: "ru"
            )
        }
    }







}