package com.example.cubetime.ui.shared

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.model.DTOs.SolveDTO
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.data.remote.SolvesAPI
import com.example.cubetime.ui.screens.settings.SettingsDataManager
import kotlinx.coroutines.launch

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

    val solvesAPI: SolvesAPI = SolvesAPI.create()



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

    suspend fun uploadSolves(solves: List<Solve>): String? {
        return solvesAPI.uploadSolves(solves.map {
            SolveDTO(
                result = it.result,
                scramble = it.scramble,
                penalty = 0,
                comment = it.comment,
                date = it.date
            )
        })
    }
    suspend fun getSolves(token: String): List<Solve>? = solvesAPI.getSolves(token)

    val _versusOpen = mutableStateOf(false)
    val versusOpen get() = _versusOpen.value
    fun changeVersusVisibility() { _versusOpen.value = !_versusOpen.value }


}

