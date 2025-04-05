package com.example.cubetime.ui.appbar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cubetime.data.ScramblesRepository
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppBarViewModel : ViewModel() {
    lateinit var sessionsList: Flow<List<Session>>
    lateinit var repository : SolvesRepository
    lateinit var scramblesRepository: ScramblesRepository

    init {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        repository = SolvesRepository.getInstance(dao)
        sessionsList = repository.sessions
        scramblesRepository = ScramblesRepository.getInstance()
    }


    private fun clearScrambles() {
        viewModelScope.launch {
            scramblesRepository.clearScrambles()
        }
    }

    fun addSession(name: String, events: Events) {
        val newSession = Session(name, events, "")
        repository.addSession(newSession)
    }

    fun deleteSession(session: Session) {
//        val idToDelete = _sessions.indexOf(session)
//        if (idToDelete <= _currentSessionID.value) {
//            _currentSessionID.value -= 1
//        }
//        _sessions.remove(session)
    }

    fun switchSessions(sessionName: String) {
        clearScrambles()
        repository.updateCurrentSessionByName(sessionName)
        viewModelScope.launch {
            scramblesRepository.updateNextScramble(repository.currentSession.value.event)
        }
    }

    fun renameSession(session: Session, index: Int, newName: String) {
//        val newSession = session.copy(name = newName)
//        _sessions[index] = newSession
    }
}