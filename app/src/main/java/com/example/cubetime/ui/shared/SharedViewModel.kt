package com.example.cubetime.ui.shared

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Session
import com.example.cubetime.data.model.Solve
import com.example.cubetime.ui.screens.settings.SettingsDataManager
import com.example.cubetime.ui.screens.timer.TimerController
import com.example.cubetime.ui.screens.settings.TimerSettings
import com.example.cubetime.utils.Scrambler
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel : ViewModel() {
    val _settingsScreenOpen = mutableStateOf(false)
    val settingsScreenOpen get() = _settingsScreenOpen.value

    private val _everythingHidden = mutableStateOf(false)
    val everythingHidden get() = _everythingHidden.value

    fun changeSettingsVisibility() {
        _settingsScreenOpen.value = !_settingsScreenOpen.value
    }

    fun hideEverything(hide: Boolean) {
        _everythingHidden.value = hide
    }

//    //режим удаления ( вынес топбара вместо основного бара)
//    private val _longPressMode = mutableStateOf(false)
//    val longPressMode: Boolean get() = _longPressMode.value
    //private val _selectedSolveIds = mutableStateListOf<String>()
//    val selectedSolveIds: List<String> get() = _selectedSolveIds
//
//    fun enableDeleteMode(firstId: String) {
//        _longPressMode.value = true
//        _selectedSolveIds.clear()
//        _selectedSolveIds.add(firstId)
//    }
//
//
//    fun addIDInSolvesLits(id: String) {
//        if (_selectedSolveIds.contains(id)) {
//            _selectedSolveIds.remove(id)
//        }
//        else {
//            _selectedSolveIds.add(id)
//        }
//    }
//
//    fun disableDeleteMode() {
//        _longPressMode.value = false
//        _selectedSolveIds.clear()
//    }
//    fun deleteSelectedSolves() {
//        val indexToRemove = _solves.mapIndexedNotNull { index, solve ->
//            index.takeIf { solve.id in _selectedSolveIds }
//        }
//
//        indexToRemove.reversed().forEach { index ->
//            _solves.removeAt(index)
//        }
//
//        _selectedSolveIds.clear()
//        _longPressMode.value = false
//
//    }



}

