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
import com.example.cubetime.ui.settings.TimerSettings
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    private var timerSettings = mutableStateOf(TimerSettings(false, false, false))
    var hideEverything: (Boolean) -> Unit = {}


    fun init (settings: TimerSettings, hideEverything: (Boolean) -> Unit) {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        solvesRepository = SolvesRepository.getInstance(dao)
        sessionsList = solvesRepository.sessions
        currentSession = solvesRepository.currentSession
        scramblesRepository = ScramblesRepository.getInstance()
        _currentScramble = scramblesRepository.currentScramble
        timerSettings.value = settings
        this.hideEverything = {hide -> hideEverything(hide)}
        updateCurrentScramble()
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

    private val _currentImage = mutableStateOf<String?>("")
    val currentImage get() = _currentImage.value


    fun inputScramble(scramble: String) {
        scramblesRepository.addScramble(scramble)
    }

    fun updateImage() {
        viewModelScope.launch(Dispatchers.Default) {
            val pictureString = Scrambler().createScramblePicture(
                currentScramble,
                currentSession.value.event
            )
            withContext(Dispatchers.Main) {
                _currentImage.value = pictureString
            }
        }
    }

    fun updateCurrentScramble() {
        viewModelScope.launch {
            Log.d("TimerViewModel", currentSession.value.event.toString())
            scramblesRepository.updateNextScramble(currentSession.value.event)
            updateImage()
        }
    }

    fun deleteLastSolve() {
        solvesRepository.deleteLastSolve()
    }

    fun addSolve(time: Int, penalty: Penalties) {
        solvesRepository.addSolve(
            Solve(
                id = 0,
                sessionName = currentSession.value.name,
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
}