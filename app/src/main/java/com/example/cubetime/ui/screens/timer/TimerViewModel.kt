package com.example.cubetime.ui.screens.timer

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.ScramblesRepository
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.SolvesRepository
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Session
import com.example.cubetime.data.model.Solve
import com.example.cubetime.ui.screens.settings.Settings
import com.example.cubetime.ui.screens.statistics.CurrentStatsUI
import com.example.cubetime.utils.DateUtils
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    lateinit private var sessionsList: Flow<List<Session>>
    lateinit private var solvesRepository : SolvesRepository
    lateinit private var scramblesRepository: ScramblesRepository
    lateinit var currentSession: MutableStateFlow<Session>
    var _currentScramble = mutableStateOf<String> ("")
    val currentScramble get() = _currentScramble.value
    var _currentImage = mutableStateOf<String?> ("")
    val currentImage get() = _currentImage.value
    private var settings = mutableStateOf(Settings(false, false, false))
    var hideEverything: (Boolean) -> Unit = {}
    var setGeneratingState: (Boolean) -> Unit = {}

    val _averages = MutableStateFlow<CurrentStatsUI>(CurrentStatsUI())
    val averages = _averages.asStateFlow()

    val _PBs = MutableStateFlow<CurrentStatsUI>(CurrentStatsUI())
    val PBs = _PBs.asStateFlow()


    val _solvesCounter = MutableStateFlow<Int>(0)
    val solvesCounter = _solvesCounter.asStateFlow()



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

        viewModelScope.launch (Dispatchers.Default) {
            solvesRepository.currentAverages.collect { newAverages ->
                _averages.update { TimeFormat.mapToUIStats(newAverages) }
            }
        }

        viewModelScope.launch (Dispatchers.Default) {
            solvesRepository.bestAverages.collect { newAverages ->
                _PBs.update { TimeFormat.mapToUIStats(newAverages) }
            }
        }

        viewModelScope.launch (Dispatchers.Default) {
            solvesRepository.solvesCounter.collect { newCounter ->
                _solvesCounter.update { newCounter }
            }
        }
    }

    init {
        Log.d("TimerVM", "created")
    }

    fun updateTimerSettings(settings: Settings) {
        this.settings.value = settings
    }

    private val _timer = mutableStateOf(
        TimerController(
            generateScr = { updateCurrentScramble() },
            addSolve = { time, penalty -> addSolve(time, penalty) },
            settings = settings,
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
                date = DateUtils.getCurrentDate(),
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