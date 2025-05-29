package com.example.cubetime.ui.screens.shared_solves


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.entities.Session
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.data.remote.SolvesAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SharedSolvesViewModel: ViewModel() {

    lateinit var repository : SolvesRepository

    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow<Boolean>(false)
    val isError = _isError.asStateFlow()

    private val _solves = MutableStateFlow<List<Solve>>(listOf())
    val solves = _solves.asStateFlow()


    private val solvesAPI: SolvesAPI = SolvesAPI.create()

    init {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        repository = SolvesRepository.getInstance(dao)
    }


    fun updateSolves(token: String) = viewModelScope.launch {
        _isError.value = false
        _isLoading.value = true
        val result: List<Solve>? = solvesAPI.getSolves(token)
        if (result == null || result == emptyList<Solve>()) {
            _isLoading.value = false
            _isError.value = true
        } else {
            _isError.value = false
            _isLoading.value = false
            _solves.value = result
        }
        _isLoading.value = false
    }

    fun addSolves(session: Session) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateCurrentSessionById(session.id)
            repository.addSolves(solves.value)
        }
    }
}