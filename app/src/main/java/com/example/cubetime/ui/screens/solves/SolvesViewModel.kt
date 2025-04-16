package com.example.cubetime.ui.screens.solves

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.ScramblesRepository
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Session
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.Solve
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SolvesViewModel : ViewModel() {
    var solvesList: Flow<List<ShortSolve>> = MutableStateFlow<List<ShortSolve>>(emptyList())
    var _chosenSolve = mutableStateOf<Solve?>(null)
    val chosenSolve get() = _chosenSolve.value
    var repository: SolvesRepository


    init {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        repository = SolvesRepository.getInstance(dao)
        solvesList = repository.shortSolves
    }

    fun chooseSolveById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _chosenSolve.value = repository.getSolveById(id)
        }
    }

    fun unchooseSolve() {
        _chosenSolve.value = null
    }

    fun updateComment(id: Int, new: String) = repository.updateComment(id, new, false)
    fun updatePenalty(id: Int, new: Penalties) = repository.updatePenalty(id, new, false)
    fun deleteSolve(id: Int) = repository.deleteSolveById(id)


    private val _longPressMode = mutableStateOf(false)
    val longPressMode: Boolean get() = _longPressMode.value

    private val _selectedSolveIds = mutableStateListOf<Int>()
    val selectedSolveIds: List<Int> get() = _selectedSolveIds

    fun enableDeleteMode(id: Int) {
        if (!_selectedSolveIds.contains(id)) {
            _selectedSolveIds.add(id)
        } else {
            _selectedSolveIds.remove(id)
        }
        _longPressMode.value = true
    }

    fun disableDeleteMode() {
        _selectedSolveIds.clear()
        _longPressMode.value = false

    }

    fun deleteSelectedSolves() {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedSolveIds.forEach { id ->
                repository.deleteSolveById(id)
            }
            _selectedSolveIds.clear()
        }
    }

    fun disableDeleteMode2() {
        _longPressMode.value = false
    }
}