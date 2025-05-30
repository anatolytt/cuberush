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
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
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
        val solves = solvesDao.getAllShortSessionSolves(session.id)
        Log.d("solvesUpdated", "solvesUpdated")
        Log.d("newSolvesShort", solves.first().toString())

        solves

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

    fun init() {
        coroutineScope.launch (Dispatchers.IO) {
            if (solvesDao.getSessionCount() == 0) {
                addSession(Session(0, "Main", Events.CUBE333, ""))
            }

            Log.d("Init", solvesDao.getSessionId("Main").toString())
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

    fun addSolves(solves: List<Solve>){
        coroutineScope.launch (Dispatchers.IO) {
            val newSolves = solves.map {solve: Solve ->
                solve.copy(sessionId = currentSession.value.id)
            }
            solvesDao.insertSolves(newSolves)
            var stats: List<solvesAverages> = listOf()
            newSolves.forEach { solve ->
                stats = statisticsManager.addSolve(
                ShortSolve(
                    id = solve.id,
                    result = solve.result,
                    penalties = solve.penalties
                )
            )}
            updateStats(stats)
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
            val solves = solvesDao.getAllShortSessionSolves(currentSession.value.id)
            updateStats(stats = statisticsManager.init(solves.first(), currentSession.value.id))
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

    suspend fun addSession(session: Session) {
        solvesDao.insertSession(session)
        Log.d("session added", "true")
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
        Log.d("UpdateCurrentSession", "вызвано")
        ScramblesRepository.getInstance().clearScrambles()
        try {
            val deferred = async (Dispatchers.IO) { currentSession.value = solvesDao.getSessionById(id) }
            deferred.await()
            //Log.d("Session ids", "P:${shortSolvesSessionId.value} Real:${currentSession.value.id}")
            updateStatManager()
            Log.d("Current session", (currentSession.value == null).toString())
            ScramblesRepository.getInstance().updateNextScramble(currentSession.value.event)
        } catch (e: Exception) {
            Log.e("SolvesRepository", "Session update exception", e)
        }
    }

    fun getAllSessionSolves() : Flow<List<Solve>>{
        return solvesDao.getAllSessionSolves(currentSession.value.id)
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