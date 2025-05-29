package com.example.cubetime.ui.screens.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("datastore")


class SettingsDataManager(val context: Context) {
    private val themeKey = booleanPreferencesKey("ThemeApp")
    private val languageKey = stringPreferencesKey("LanguageApp")
    private val inspectionKey = booleanPreferencesKey("inspection_enabled")
    private val timehiddenKey = booleanPreferencesKey("time_hidden") // скрывать / не скрывать время
    private val delayKey = booleanPreferencesKey("delay") //задержка перед стартом
    private val printScramblesKey = booleanPreferencesKey("print_scrambles")

    suspend fun  <T> saveSetting(key: Preferences.Key<T>, newValue: T){
        context.dataStore.edit { pref ->
            pref[key] = newValue
        }
    }

    suspend fun setTheme(value: Boolean) {
        saveSetting(themeKey, value)
    }

    suspend fun setLanguage(value: String) {
        saveSetting(languageKey, value)
    }

    suspend fun setInspection(value: Boolean) {
        saveSetting(inspectionKey, value)
    }

    suspend fun setTimeIsHidden(value: Boolean) {
        saveSetting(timehiddenKey, value)
    }

    suspend fun setDelay (value: Boolean) {
        saveSetting(delayKey, value)
    }

    suspend fun setPrintScrambles(value: Boolean) {
        saveSetting(printScramblesKey, value)
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
    fun getInspection(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[inspectionKey] ?: false
        }
    }
    fun getTimeHidden(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[timehiddenKey] ?: false
        }
    }
    fun getDelay(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[delayKey] ?: false
        }
    }

    fun getPrintScrambles() : Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[printScramblesKey] ?: false
        }
    }

    fun getTimerSettings() : Flow<Settings> {
        return context.dataStore.data.map { preferences ->
            Settings(
                  timerInspection = preferences[inspectionKey] ?: false,
                  timerHideTime = preferences[timehiddenKey] ?: false,
                  timerDelay = preferences[delayKey] ?: false,
                  printScrambles = preferences[printScramblesKey] ?: false
            )

        }
    }


}