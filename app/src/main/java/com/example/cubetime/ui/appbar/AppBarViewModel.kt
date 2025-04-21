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
import kotlinx.coroutines.Dispatchers
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
        viewModelScope.launch (Dispatchers.IO) {
            scramblesRepository.clearScrambles()
        }
    }

    fun addSession(name: String, events: Events) {
        val newSession = Session(0, name, events, "")
        repository.addSession(newSession)
    }

    fun deleteSession(id: Int) {
        repository.deleteSession(id)
    }

    fun renameSession(id: Int, newName: String) {
        repository.updateSessionName(id, newName)
    }

    fun switchSessions(sessionId: Int) {
        clearScrambles()
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateCurrentSessionById(sessionId)
            scramblesRepository.updateNextScramble(repository.currentSession.value.event)
        }
    }

    fun renameSession(session: Session, index: Int, newName: String) {
//        val newSession = session.copy(name = newName)
//        _sessions[index] = newSession
    }
}