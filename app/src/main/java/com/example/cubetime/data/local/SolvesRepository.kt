package com.example.cubetime.data.local

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.cubetime.data.ScramblesRepository
import com.example.cubetime.data.model.AverageResult
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.entities.Session
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.StatType
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.data.model.entities.solvesAverages
import com.example.cubetime.utils.statistics.StatisticsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



@OptIn(ExperimentalCoroutinesApi::class)
class SolvesRepository(private val solvesDao: SolvesDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val statisticsManager: StatisticsManager = StatisticsManager()

    val minState = mutableStateOf(0)
    val maxState = mutableStateOf(Int.MAX_VALUE)

    val lastSolveId = MutableStateFlow<Int>(0)
    val sessions : Flow<List<Session>> = solvesDao.getAllSessions()
    var currentSession = MutableStateFlow<Session>(Session(0, "", Events.CUBE333, ""))
    var shortSolves: Flow<List<ShortSolve>> = currentSession.flatMapLatest { session ->
        solvesDao.getAllShortSessionSolves(session.id)
    }

    val solvesCounter: Flow<Int> = currentSession.flatMapLatest { session ->
        solvesDao.countSolves(session.id)
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



    var bestAverages = MutableStateFlow<Map<StatType, AverageResult>>(mapOf())

    var currentAverages = MutableStateFlow<Map<StatType, AverageResult>>(mapOf())

    init {
        coroutineScope.launch {
            if (solvesDao.getSessionCount() == 0) {
                addSession(Session(0, "Main", Events.CUBE333, ""))
            }
            updateCurrentSessionById(solvesDao.getSessionId("Main"))

        }
    }

    fun addSolve(solve: Solve) {
        coroutineScope.launch (Dispatchers.IO) {
            solvesDao.insertSolve(solve)
            lastSolveId.update { solvesDao.getLastSolveId(currentSession.value.id) }
            Log.d("Last solve:", solvesDao.getSolveById(lastSolveId.value).result.toString())
            updateStats(stats = statisticsManager.addSolve(
                ShortSolve(
                    id = lastSolveId.value,
                    result = solve.result,
                    penalties = solve.penalties
                )
            ))
        }
    }

    private fun updateStats(stats: List<solvesAverages>) {
        solvesDao.updateSolvesAverages(stats)
        bestAverages.update {
            solvesDao.getAllSessionBestAverages(currentSession.value.id)
        }
        currentAverages.update {
            solvesDao.getAllSessionCurrentAverages(currentSession.value.id)
        }
    }




    private fun updateStatManager() {
         coroutineScope.launch {
            shortSolves.collectLatest {solves ->
                updateStats(stats = statisticsManager.init(solves, currentSession.value.id))
                cancel()
            }
        }

    }

    fun getSolveById(id: Int): Solve {
        return solvesDao.getSolveById(id)
    }


    suspend fun getAverageSolves(statType: StatType, isBest: Boolean): List<Solve> {
        return solvesDao?.getAverageSolves(
            sessionId = currentSession.value.id,
            solvesInAvg = statType.getSolvesInAvg(),
            lastSolveId = (
                    if (isBest) bestAverages
                    else currentAverages
                    ).value.get(statType)?.solveId ?: 0
         ) ?: listOf()
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
            val solveId = if (lastSolve) { lastSolveId.value } else { id }
            solvesDao.updatePenalty(solveId, new)
            recalculateStats(start = solveId, end = solveId)
        }
    }


    private fun recalculateStats(start: Int, end: Int) {
        coroutineScope.launch {
            shortSolves.collectLatest { solves ->
                Log.d("solvesAfterDelete", solves.toString())
                updateStats(statisticsManager.recalculateAndGetBests(
                    initSolves = solves,
                    start = start,
                    end = end)
                )
                cancel()
            }
        }
    }


    fun deleteLastSolve() {
        coroutineScope.launch (Dispatchers.IO) {
            val idToDelete = lastSolveId.value
            solvesDao.deleteSolvesById(listOf(idToDelete))
            recalculateStats(start = idToDelete, end = idToDelete)

        }
    }

    fun deleteSolvesById(ids: List<Int>) {
        coroutineScope.launch (Dispatchers.IO) {
            Log.d("ids here", ids.toString())
            solvesDao.deleteSolvesById(ids)
            recalculateStats(start = ids.min(), end = ids.max())
        }
    }

    fun addSession(session: Session) {
        coroutineScope.launch (Dispatchers.IO) {
            solvesDao.insertSession(session).toInt()
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

    fun updateCurrentSessionById(id: Int) = coroutineScope.launch(Dispatchers.IO) {
        ScramblesRepository.getInstance().clearScrambles()
        Log.d("вызвано!", currentSession.value.event.toString())
        try {
            val deferred = async (Dispatchers.IO) { currentSession.value = solvesDao.getSessionById(id) }
            deferred.await()
            updateStatManager()
            ScramblesRepository.getInstance().updateNextScramble(currentSession.value.event)
        } catch (e: Exception) {
            Log.e("SolvesRepository", "Session update exception", e)
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