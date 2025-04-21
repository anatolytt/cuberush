package com.example.cubetime.ui.screens.timer

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.ScramblesRepository
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Session
import com.example.cubetime.data.model.Solve
import com.example.cubetime.ui.screens.settings.TimerSettings
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimerViewModel : ViewModel() {
    lateinit private var sessionsList: Flow<List<Session>>
    lateinit private var solvesRepository : SolvesRepository
    lateinit private var scramblesRepository: ScramblesRepository
    lateinit var currentSession: MutableStateFlow<Session>
    var _currentScramble = mutableStateOf<String> ("")
    val currentScramble get() = _currentScramble.value
    var _currentImage = mutableStateOf<String?> ("")
    val currentImage get() = _currentImage.value
    private var timerSettings = mutableStateOf(TimerSettings(false, false, false))
    var hideEverything: (Boolean) -> Unit = {}
    var setGeneratingState: (Boolean) -> Unit = {}


    fun init (
        hideEverything: (Boolean) -> Unit,
        setGeneratingState: (Boolean) -> Unit) {

        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        solvesRepository = SolvesRepository.getInstance(dao)
        sessionsList = solvesRepository.sessions
        currentSession = solvesRepository.currentSession
        scramblesRepository = ScramblesRepository.getInstance()
        _currentScramble = scramblesRepository.currentScramble
        _currentImage = scramblesRepository.currentImage
        this.hideEverything = {hide -> hideEverything(hide)}
        this.setGeneratingState = {state -> setGeneratingState(state)}
        updateCurrentScramble()
    }

    init {
        Log.d("TimerVM", "created")
    }

    fun updateTimerSettings(settings: TimerSettings) {
        timerSettings.value = settings
    }

    private val _timer = mutableStateOf(
        TimerController(
            generateScr = { updateCurrentScramble() },
            addSolve = { time, penalty -> addSolve(time, penalty) },
            settings = timerSettings,
            hideEverything = {hide -> hideEverything(hide)}
        )
    )
    val timer get() = _timer.value


    fun inputScramble(scramble: String) {
        scramblesRepository.addCustomScramble(scramble)
    }


    fun updateCurrentScramble() {
        viewModelScope.launch (Dispatchers.IO) {
            setGeneratingState(true)
            scramblesRepository.updateNextScramble(currentSession.value.event)
            Log.d("TimerVM","updated")
            setGeneratingState(false)
        }
    }

    fun deleteLastSolve() {
        timer.clear()
        solvesRepository.deleteLastSolve()
    }

    fun addSolve(time: Int, penalty: Penalties) {
        solvesRepository.addSolve(
            Solve(
                id = 0,
                sessionId = currentSession.value.id,
                result = time,
                event = currentSession.value.event,
                penalties = penalty,
                date = "",
                scramble = currentScramble,
                comment = "",
                reconstruction = "",
                isCustomScramble = false,
            )
        )

    }

    fun updateComment(id: Int, new: String) = solvesRepository.updateComment(id, new, lastSolve=true)
    fun updatePenalty(id: Int, new: Penalties) = solvesRepository.updatePenalty(id, new, lastSolve=true)
}