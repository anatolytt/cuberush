package com.example.cubetime.data.local

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getString
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
class SolvesRepository(private val solvesDao: SolvesDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val minState = mutableStateOf(0)
    val maxState = mutableStateOf(Int.MAX_VALUE)

    val lastSolveId = MutableStateFlow<Int>(0)
    val sessions: Flow<List<Session>> = solvesDao.getAllSessions()
    var currentSession = MutableStateFlow<Session>(Session(0, "", Events.CUBE333, ""))
    var shortSolves: Flow<List<ShortSolve>> = currentSession.flatMapLatest { session ->
        solvesDao.getAllShortSessionSolves(min = minState.value, max = maxState.value, session.id)
    }

    fun changeMinMaxSearch(min: Int, max: Int) {
        minState.value = min
        maxState.value = max
        shortSolves =
            solvesDao.getAllShortSessionSolves(
                min = minState.value,
                max = maxState.value,
                currentSession.value.id
            )
    }
    //сортировка по убыванию
    fun sortSolvesListEnd()
    {
        shortSolves = solvesDao.getSortedShortSolveEnd(currentSession.value.id)
    }
    //сортировка по возрастанию
    fun sortSolvesListStart()
    {
        shortSolves = solvesDao.getSortedShortSolveStart(currentSession.value.id)
    }


    init {
        coroutineScope.launch {
            if (solvesDao.getSessionCount() == 0) {
                addSession(Session(0, "Main", Events.CUBE333, ""))
            } else {
                updateCurrentSessionById(solvesDao.getSessionId("Main"))
            }
        }
    }

    fun addSolve(solve: Solve) {
        coroutineScope.launch(Dispatchers.IO) {
            solvesDao.insertSolve(solve)
            lastSolveId.update { solvesDao.getLastSolveId() }
            Log.d("Solves", solve.id.toString())
        }
    }

    fun getSolveById(id: Int): Solve {
        return solvesDao.getSolveById(id)
    }


    fun updateComment(id: Int = 0, new: String, lastSolve: Boolean) {
        coroutineScope.launch(Dispatchers.IO) {
            if (lastSolve) {
                solvesDao.updateComment(lastSolveId.value, new)
            } else {
                solvesDao.updateComment(id, new)
            }
        }
    }

    fun updatePenalty(id: Int = 0, new: Penalties, lastSolve: Boolean) {
        coroutineScope.launch(Dispatchers.IO) {
            if (lastSolve) {
                solvesDao.updatePenalty(lastSolveId.value, new)
            } else {
                solvesDao.updatePenalty(id, new)
            }
        }
    }

    fun deleteLastSolve() {
        coroutineScope.launch(Dispatchers.IO) {
            Log.d("SolvesRepository", lastSolveId.value.toString())
            solvesDao.deleteById(lastSolveId.value)
        }
    }

    fun deleteSolveById(id: Int) {
        coroutineScope.launch {
            solvesDao.deleteById(id)
        }
    }


    fun addSession(session: Session) {
        coroutineScope.launch(Dispatchers.IO) {
            val id: Int = solvesDao.insertSession(session).toInt()
            updateCurrentSessionById(id)
        }
    }

    fun deleteSession(id: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            solvesDao.deleteSessionById(id)
            solvesDao.deleteSessionSolves(id)
            updateCurrentSessionById(solvesDao.getLastAddedSessionId())
        }
    }

    fun updateSessionName(id: Int, newName: String) {
        coroutineScope.launch(Dispatchers.IO) {
            solvesDao.updateSessionName(id, newName)
            updateCurrentSessionById(id)
        }
    }

    suspend fun updateCurrentSessionById(id: Int) {
        ScramblesRepository.getInstance().clearScrambles()
        currentSession.value = solvesDao.getSessionById(id)
        ScramblesRepository.getInstance().updateNextScramble(currentSession.value.event)
        delay(200)
        changeMinMaxSearch(0, Int.MAX_VALUE)


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
                return instance
            }
        }
    }
}