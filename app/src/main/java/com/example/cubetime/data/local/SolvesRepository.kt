package com.example.cubetime.data.local

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.cubetime.data.ScramblesRepository
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Session
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.Solve
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
class SolvesRepository(private val solvesDao: SolvesDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val lastSolveId = MutableStateFlow<Int>(0)
    val sessions : Flow<List<Session>> = solvesDao.getAllSessions()
    var currentSession =  MutableStateFlow<Session>(Session("", Events.SQ1, ""))
    val shortSolves: Flow<List<ShortSolve>> = currentSession.flatMapLatest { session ->
        solvesDao.getAllShortSessionSolves(session.name)
    }

    init {
        addSession(Session("Main", Events.CUBE333, ""))
    }

    fun addSolve(solve: Solve) {
        coroutineScope.launch (Dispatchers.IO) {
            solvesDao.insertSolve(solve)
            lastSolveId.update { solvesDao.getLastSolveId() }
            Log.d("Solves", solve.id.toString())

        }
    }

    fun getSolveById(id: Int): Solve {
        return solvesDao.getSolveById(id)
    }

    fun updateComment(id: Int=0, new: String, lastSolve: Boolean) {
        coroutineScope.launch (Dispatchers.IO) {
            if (lastSolve) {
                solvesDao.updateComment(lastSolveId.value, new)
            } else {
                solvesDao.updateComment(id, new)
            }
        }
    }

    fun updatePenalty(id: Int=0, new: Penalties, lastSolve: Boolean) {
        coroutineScope.launch (Dispatchers.IO) {
            if (lastSolve) {
                solvesDao.updatePenalty(lastSolveId.value, new)
            } else {
                solvesDao.updatePenalty(id, new)
            }
        }
    }

    fun addSession(session: Session) {
        coroutineScope.launch (Dispatchers.IO) {
            solvesDao.insertSession(session)
            updateCurrentSessionByName(session.name)
        }
    }

    fun updateCurrentSessionByName(name: String) {
        Log.d("Solves", "вызвано")
        coroutineScope.launch (Dispatchers.IO) {
            ScramblesRepository.getInstance().clearScrambles()
            currentSession.value = solvesDao.getSessionByName(name)
            ScramblesRepository.getInstance().updateNextScramble(currentSession.value.event)
        }
    }

    fun deleteLastSolve() {
        coroutineScope.launch (Dispatchers.IO) {
            Log.d("SolvesRepository", lastSolveId.value.toString())
            solvesDao.deleteById(lastSolveId.value)
        }
    }

    fun deleteSolveById(id: Int) {
        coroutineScope.launch {
            solvesDao.deleteById(id)
        }
    }

    companion object {
        private var INSTANCE: SolvesRepository? = null
        fun getInstance(
            solvesDao: SolvesDao
        ): SolvesRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = SolvesRepository(solvesDao)
                }
                INSTANCE = instance
                return  instance
            }
        }
    }
}