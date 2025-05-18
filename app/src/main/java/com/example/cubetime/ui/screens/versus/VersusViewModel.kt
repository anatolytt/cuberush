package com.example.cubetime.ui.screens.versus

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.entities.Session
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.ui.screens.settings.Settings
import com.example.cubetime.ui.screens.timer.TimerController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VersusViewModel: ViewModel() {
    lateinit var repository1: SolvesRepository
    lateinit var repository2: SolvesRepository

    var currentSolves: MutableMap<Int, Pair<Int, Penalties>?> = mutableMapOf(1 to null, 2 to null)
    var currentScramble = mutableStateOf("")
    private var settings = mutableStateOf(Settings(false, false, false))

    private val _timer1 = mutableStateOf(
        TimerController(
            generateScr = { },
            //addSolve = { time, penalty -> addSolve(time, penalty) },
            addSolve = { time, penalty ->
                currentSolves[1] = Pair(time, penalty)
                addSolves()
            },
            settings = settings,
            hideEverything = { }
        )
    )
    val timer1 get() = _timer1.value

    private val _timer2 = mutableStateOf(
        TimerController(
            generateScr = { },
            addSolve = { time, penalty ->
                currentSolves[2] = Pair(time, penalty)
                addSolves()
            },
            settings = settings,
            hideEverything = { }
        )
    )
    val timer2 get() = _timer2.value

    init {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        repository1 = SolvesRepository(dao)
        repository2 = SolvesRepository(dao)
    }

    fun setSessions(session1: Session, session2: Session) {
        viewModelScope.launch (Dispatchers.IO) {
            repository1.updateCurrentSessionById(session1.id)
            repository2.updateCurrentSessionById(session2.id)
        }
    }

    fun addSolves() {
        if (currentSolves[1] != null && currentSolves[2] != null) {
            repository1.addSolve(Solve(
                id = 0,
                sessionId = repository1.currentSession.value.id,
                result = currentSolves[1]!!.first,
                event = repository1.currentSession.value.event,
                penalties = currentSolves[1]!!.second,
                date = "",
                scramble = currentScramble.value,
                comment = "",
                reconstruction = "",
                isCustomScramble = false
            ))

            repository2.addSolve(Solve(
                id = 0,
                sessionId = repository2.currentSession.value.id,
                result = currentSolves[2]!!.first,
                event = repository2.currentSession.value.event,
                penalties = currentSolves[2]!!.second,
                date = "",
                scramble = currentScramble.value,
                comment = "",
                reconstruction = "",
                isCustomScramble = false
            ))
            currentSolves[1] = null
            currentSolves[2] = null
        }
    }

    fun updateTimerSettings(settings: Settings) {
        this.settings.value = settings
    }

}