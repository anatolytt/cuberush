package com.example.cubetime.ui.screens.timer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import com.example.cubetime.model.Penalties
import com.example.cubetime.ui.settings.SettingsData
import com.example.cubetime.ui.settings.SettingsDataManager
import com.example.cubetime.ui.settings.TimerSettings
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TimerController(
    val hideEverything: (Boolean) -> Unit = {},
    val generateScr: () -> Unit = {},
    val settings: MutableState<TimerSettings>
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val TIME_HIDDEN get() = settings.value.timehidden
    val INSPECTION_ON get() = settings.value.isInspectionEnabled
    val DELAY_ON get() = settings.value.delay

    private val _currentTime = mutableStateOf<Int>(0)
    val currentTime: Int get() = _currentTime.value

    val currentTimeToShow: String get() = timeToShow()

    private val _penaltyState = mutableStateOf(Penalties.NONE)
    val penaltyState get() = _penaltyState.value

    private val _timerState = mutableStateOf<TimerState>(TimerState.INACTIVE)
    val timerState get() = _timerState.value

    private val _isFirstSolve = mutableStateOf(true)
    val isFirstSolve get() = _isFirstSolve.value

    var timerJob: Job? = null

    fun startInspection() {
        timerJob?.cancel()
        _currentTime.value = 15000
        _timerState.value = TimerState.INSPECTION
        _penaltyState.value = Penalties.NONE
        timerJob = coroutineScope.launch {
            while (timerState == TimerState.INSPECTION) {
                delay(1000)
                _currentTime.value -= 1000
                if (currentTime == 0) {
                    _penaltyState.value = Penalties.PLUS2
                }
                if (currentTime == -2000) {
                    hideEverything(false)
                    _penaltyState.value = Penalties.DNF
                    _currentTime.value = 0
                    _timerState.value = TimerState.INACTIVE
                }
            }
        }

    }

    fun startTimer() {
        timerJob?.cancel()
        _currentTime.value = 0
        _timerState.value = TimerState.GOING
        timerJob = coroutineScope.launch () {
            while (timerState == TimerState.GOING) {
                delay(10)
                _currentTime.value += 10
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.INACTIVE
        _isFirstSolve.value = false
        hideEverything(false)
        generateScr()
    }

    fun stopAndDelete() {
        timerJob?.cancel()
        _timerState.value = TimerState.INACTIVE
        _currentTime.value = 0
        _penaltyState.value = Penalties.NONE
        _isFirstSolve.value = true
        hideEverything(false)
        generateScr()
    }


    private fun timeToShow() : String {
        return when (timerState) {
            TimerState.INACTIVE -> {
                when (penaltyState) {
                    Penalties.PLUS2 -> TimeFormat.millisToString(currentTime+2000) + "+"
                    Penalties.DNF -> "DNF"
                    Penalties.NONE -> TimeFormat.millisToString(currentTime)
                }
            }
            TimerState.INSPECTION -> {
                when (penaltyState) {
                    Penalties.PLUS2 -> "+2"
                    Penalties.DNF -> "DNF"
                    Penalties.NONE -> ((currentTime/1000).toString())
                }
            }
            TimerState.GOING -> {
                if (TIME_HIDDEN) {
                    "..."
                } else {
                    TimeFormat.millisToString(currentTime)
                }
            }
        }
    }

    fun changePenalty(penalty: Penalties) {
        if (penaltyState == penalty) {
            _penaltyState.value = Penalties.NONE
        } else {
            _penaltyState.value = penalty
        }
    }

    fun inputSolve(
        solveMillis: String,
        penalty: Penalties) {
        _currentTime.value = TimeFormat.inputTextToMillis(solveMillis)
        _penaltyState.value = penalty
        _isFirstSolve.value = false
    }


    fun shortPressAction() {
        when (timerState) {
            TimerState.GOING -> {
                stopTimer()
                hideEverything(false)
            }
            TimerState.INACTIVE -> {
                if (INSPECTION_ON) {
                    hideEverything(true)
                    startInspection()
                } else if (!DELAY_ON) {
                    hideEverything(true)
                    startTimer()
                }
            }
            TimerState.INSPECTION -> {
                if (!DELAY_ON) {
                    hideEverything(true)
                    startTimer()
                }
            }
        }
    }

    fun longPressAction() {
        when (timerState) {
            TimerState.GOING -> {
                stopTimer()
                hideEverything(false)
            }
            TimerState.INACTIVE -> {
                hideEverything(true)
                if (INSPECTION_ON) {
                    hideEverything(true)
                    startInspection()
                } else {
                    hideEverything(true)
                    startTimer()
                }
            }
            TimerState.INSPECTION -> startTimer()
        }
    }





}
