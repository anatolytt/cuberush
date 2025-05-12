package com.example.cubetime.ui.shared

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cubetime.ui.screens.settings.SettingsDataManager

class SharedViewModel : ViewModel() {
    val _settingsScreenOpen = mutableStateOf(false)
    val settingsScreenOpen get() = _settingsScreenOpen.value

    private val _everythingHidden = mutableStateOf(false)
    val everythingHidden get() = _everythingHidden.value

    private val _deleteSolveAppBar = mutableStateOf(false)
    val deleteSolveAppBar get() = _deleteSolveAppBar.value

    private val _scrambleIsGenerating = mutableStateOf(false)
    val scrambleIsGenerating get() = _scrambleIsGenerating.value

    lateinit var settingsManager: SettingsDataManager


    var deleteSolves: ()->Unit = {}
    fun setSolvesDelete(func: () -> Unit) { deleteSolves = func }

    fun hideEverything(hide: Boolean) { _everythingHidden.value = hide }
    fun changeAppBar() { _deleteSolveAppBar.value = !_deleteSolveAppBar.value }
    fun changeSettingsVisibility() { _settingsScreenOpen.value = !_settingsScreenOpen.value }
    fun setGeneratingState(state: Boolean) {
        _scrambleIsGenerating.value = state
        Log.d("SharedVM", scrambleIsGenerating.toString())
    }

    fun setSettingsDataManager(settings: SettingsDataManager) {
        settingsManager = settings
    }

    val _versusOpen = mutableStateOf(false)
    val versusOpen get() = _versusOpen.value
    fun changeVersusVisibility() { _versusOpen.value = !_versusOpen.value }
}

