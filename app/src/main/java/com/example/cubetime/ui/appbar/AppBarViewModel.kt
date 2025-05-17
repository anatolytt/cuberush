package com.example.cubetime.ui.appbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.ScramblesRepository
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.entities.Session
import com.example.cubetime.ui.screens.statistics.CurrentStatsUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppBarViewModel : ViewModel() {
    private val _currentSession = MutableStateFlow<Session>(
        Session(id=0, name="Main", event = Events.CUBE333)
    )
    val currentSession = _currentSession.asStateFlow()

    init {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        val repository = SolvesRepository.getInstance(dao)
        viewModelScope.launch {
            repository.currentSession.collect { session ->
                _currentSession.value = session
            }
        }
    }

}