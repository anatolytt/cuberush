package com.example.cubetime.data.local

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getString
import com.example.cubetime.data.ScramblesRepository
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Session
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.Solve
import com.example.cubetime.utils.statistics.StatisticsManager
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
    lateinit var statisticsManager: MutableState<StatisticsManager>
    var averages: MutableMap<Int, Int> = mutableMapOf()
    var bestAverages: MutableMap<Int, Int> = mutableMapOf()

    val lastSolveId = MutableStateFlow<Int>(0)
    val sessions : Flow<List<Session>> = solvesDao.getAllSessions()
    var currentSession =  MutableStateFlow<Session>(Session(0, "", Events.CUBE333, ""))
    val shortSolves: Flow<List<ShortSolve>> = currentSession.flatMapLatest { session ->
        solvesDao.getAllShortSessionSolves(session.id)
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
        coroutineScope.launch (Dispatchers.IO) {
            solvesDao.insertSolve(solve)
            lastSolveId.update { solvesDao.getLastSolveId() }
            updateStats(solve)
            Log.d("CurrentStats", statisticsManager.value.averages.toString())
        }
    }

    fun updateStats(solve: Solve) {
        coroutineScope.launch {
            statisticsManager.value.addSolve(ShortSolve(solve.id, solve.result, solve.penalties, solve.date))
            solvesDao.updateAo5(statisticsManager.value.averages.get(5)!!, currentSession.value.id)
            solvesDao.updateAo12(statisticsManager.value.averages.get(12)!!, currentSession.value.id)
            solvesDao.updateAo25(statisticsManager.value.averages.get(25)!!, currentSession.value.id)
            solvesDao.updateAo50(statisticsManager.value.averages.get(50)!!, currentSession.value.id)
            solvesDao.updateAo100(statisticsManager.value.averages.get(100)!!, currentSession.value.id)
            statisticsManager.value.bestAverages[3] = solvesDao.getMo3(currentSession.value.id)
            statisticsManager.value.bestAverages[5] = solvesDao.getAo5(currentSession.value.id)
            statisticsManager.value.bestAverages[12] = solvesDao.getAo12(currentSession.value.id)
            statisticsManager.value.bestAverages[25] = solvesDao.getAo25(currentSession.value.id)
            statisticsManager.value.bestAverages[50] = solvesDao.getAo50(currentSession.value.id)
            statisticsManager.value.bestAverages[100] = solvesDao.getAo100(currentSession.value.id)
        }
    }

    fun getSolveById(id: Int): Solve {
        return solvesDao.getSolveById(id)
    }

    fun calculateStatistics() {

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



    fun addSession(session: Session) {
        coroutineScope.launch (Dispatchers.IO) {
            val id: Int  = solvesDao.insertSession(session).toInt()
            updateCurrentSessionById(id)
        }
    }

    fun deleteSession(id: Int) {
        coroutineScope.launch (Dispatchers.IO) {
            solvesDao.deleteSessionById(id)
            solvesDao.deleteSessionSolves(id)
            updateCurrentSessionById(solvesDao.getLastAddedSessionId())
        }
    }

    fun updateSessionName(id: Int, newName: String) {
        coroutineScope.launch (Dispatchers.IO) {
            solvesDao.updateSessionName(id, newName)
            updateCurrentSessionById(id)
        }
    }

    suspend fun updateCurrentSessionById(id: Int) {
        ScramblesRepository.getInstance().clearScrambles()
        currentSession.value = solvesDao.getSessionById(id)
        ScramblesRepository.getInstance().updateNextScramble(currentSession.value.event)

        delay(500)

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