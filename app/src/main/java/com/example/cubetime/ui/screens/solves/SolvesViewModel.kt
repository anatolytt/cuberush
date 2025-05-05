package com.example.cubetime.ui.screens.solves

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.SolvesRepository
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.Solve
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SolvesViewModel : ViewModel() {
    var solvesList: MutableStateFlow<List<ShortSolve>> = MutableStateFlow<List<ShortSolve>>(emptyList())
    var _chosenSolve = mutableStateOf<Solve?>(null)
    val chosenSolve get() = _chosenSolve.value
    var repository: SolvesRepository


    init {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        repository = SolvesRepository.getInstance(dao)
        viewModelScope.launch {
            repository.shortSolves.collect{ newList ->
                solvesList.value = newList
            }
        }
    }

    fun chooseSolveById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _chosenSolve.value = repository.getSolveById(id)
        }
    }

    fun unchooseSolve() {
        _chosenSolve.value = null
    }

    fun deleteSolve(id: Int) {
        Log.d("CalledDeleteSolve", "")
        repository.deleteSolvesById(listOf(id))


    }

    fun updateComment(id: Int, new: String) = repository.updateComment(id, new, false)
    fun updatePenalty(id: Int, new: Penalties) = repository.updatePenalty(id, new, false)


    val scrollPosition = mutableStateOf(0)

    private val _selectedSolveIds = mutableStateListOf<Int>()
    val selectedSolveIds: List<Int> get() = _selectedSolveIds

    fun enableDeleteMode(id: Int) {
        if (!_selectedSolveIds.contains(id)) {
            _selectedSolveIds.add(id)
        } else {
            _selectedSolveIds.remove(id)
        }
    }

    fun deleteSelectedSolves() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSolvesById(selectedSolveIds.toList())
            _selectedSolveIds.clear()
        }
    }



}