package com.example.cubetime.ui.session_dialogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.ScramblesRepository
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.entities.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SessionDialogsViewModel: ViewModel() {
    private var _sessionsList =  MutableStateFlow<List<Session>>(emptyList())
    var sessionsList = _sessionsList.asStateFlow()
    lateinit var repository : SolvesRepository
    lateinit var scramblesRepository: ScramblesRepository

    init {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        repository = SolvesRepository.getInstance(dao)
        viewModelScope.launch {
            repository.sessions.collect { list ->
                _sessionsList.value = list
            }
        }

        scramblesRepository = ScramblesRepository.getInstance()
    }


    fun addSession(name: String, events: Events) {
        viewModelScope.launch (Dispatchers.IO) {
            val newSession = Session(0, name, events, "")
            repository.addSession(newSession)
            _sessionsList.first{it.last().name == name}
            switchSessions(_sessionsList.value.last().id)
        }
    }

    fun deleteSession(id: Int) {
        repository.deleteSession(id)
    }

    fun renameSession(id: Int, newName: String) {
        repository.updateSessionName(id, newName)
    }

    fun switchSessions(sessionId: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateCurrentSessionById(sessionId)
        }
    }

}