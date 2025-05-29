package com.example.cubetime.ui.screens.timer

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.ui.screens.settings.Settings
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.util.concurrent.locks.LockSupport

class TimerController(
    val generateScr: () -> Unit,
    val hideEverything: (Boolean) -> Unit,
    val settings: MutableState<Settings>,
    val addSolve: (Int, Penalties) -> Unit
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val TIME_HIDDEN get() = settings.value.timerHideTime
    val INSPECTION_ON get() = settings.value.timerInspection
    val DELAY_ON get() = settings.value.timerDelay

    private val _currentTime = mutableStateOf<Int>(0)
    val currentTime: Int get() = _currentTime.value

    val currentTimeToShow: String get() = timeToShow()

    private val _penaltyState = mutableStateOf(Penalties.NONE)
    val penaltyState get() = _penaltyState.value

    private val _timerState = mutableStateOf<TimerState>(TimerState.INACTIVE)
    val timerState get() = _timerState.value

    private val _isFirstSolve = mutableStateOf(true)
    val isFirstSolve get() = _isFirstSolve.value

    private val _delayAfterStopOn = mutableStateOf(false)
    val delayAfterStopOn get() = _delayAfterStopOn.value

    var timerJob: Job? = null



    fun startInspection() {
        timerJob?.cancel()
        _currentTime.value = 15000
        _timerState.value = TimerState.INSPECTION
        _penaltyState.value = Penalties.NONE
        timerJob = coroutineScope.launch {
            while (timerState == TimerState.INSPECTION) {
                measureTime(1000)
                ensureActive()
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
        _penaltyState.value = Penalties.NONE
        timerJob = coroutineScope.launch () {
            while (timerState == TimerState.GOING) {
                measureTime(10)
                ensureActive()
                _currentTime.value += 10
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.INACTIVE
        _isFirstSolve.value = false
        hideEverything(false)
        addSolve(currentTime, penaltyState)
        generateScr()

        // Задержка после остановки таймера
        coroutineScope.launch {
            _delayAfterStopOn.value = true
            measureTime(150)
            _delayAfterStopOn.value = false
        }


    }

    fun stopAndDelete() {
        timerJob?.cancel()
        _timerState.value = TimerState.INACTIVE
        clear()
        Log.d("stop", _currentTime.value.toString() + " " + timeToShow())
        coroutineScope.launch {  delay(100)
            Log.d("stop", _currentTime.value.toString() + " " + timeToShow())
        }

        hideEverything(false)
    }

    fun clear() {
        _currentTime.value = 0
        _penaltyState.value = Penalties.NONE
        _isFirstSolve.value = true
        generateScr()
    }


    private fun timeToShow() : String {
        return when (timerState) {
            TimerState.INACTIVE -> {
                TimeFormat.millisToString(
                    millis = currentTime,
                    penalty = penaltyState
                )
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
                    TimeFormat.millisToString(
                        millis = currentTime,
                        penalty = penaltyState
                    )
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
        penalty: Penalties
    ) {
        _currentTime.value = TimeFormat.inputTextToMillis(solveMillis)
        _penaltyState.value = penalty
        addSolve(currentTime, penaltyState)
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

    private fun measureTime(timeMillis: Int) {
        val start = System.nanoTime()
        val target = start + timeMillis * 1_000_000L
        while (System.nanoTime() < target) { }
    }

}
