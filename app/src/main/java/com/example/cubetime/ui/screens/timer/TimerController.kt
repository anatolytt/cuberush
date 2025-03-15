package com.example.cubetime.ui.screens.timer

import androidx.compose.runtime.mutableStateOf
import com.example.cubetime.model.Penalties
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerController (
        val hideEverything : (Boolean) -> Unit,
        val generateScr: () -> Unit) {
    val TIME_HIDDEN = false
    val INSPECTION_ON = true
    val DELAY_ON = false

    private val _currentTime = mutableStateOf<Int>(0)
    val currentTime: Int get() = _currentTime.value

    val currentTimeToShow get() = timeToShow()


    private val _inspectionPenaltyState = mutableStateOf(Penalties.NONE)
    val inspectionPenaltyState get() = _inspectionPenaltyState.value

    private val _timerState = mutableStateOf<TimerState>(TimerState.INACTIVE)
    val timerState get() = _timerState.value

    var timerJob: Job? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)



    fun startInspection() {
        timerJob?.cancel()
        hideEverything(true)
        _currentTime.value = 15000
        _timerState.value = TimerState.INSPECTION
        timerJob = coroutineScope.launch {
            while (timerState == TimerState.INSPECTION) {
                delay(1000)
                _currentTime.value -= 1000
                if (currentTime == 0) {
                    _inspectionPenaltyState.value = Penalties.PLUS2
                }
                if (currentTime == -2000) {
                    hideEverything(false)
                    _inspectionPenaltyState.value = Penalties.DNF
                    _timerState.value = TimerState.INACTIVE
                }
            }
        }

    }

    fun startTimer() {
        timerJob?.cancel()
        hideEverything(true)
        _currentTime.value = 0
        _timerState.value = TimerState.GOING
        _inspectionPenaltyState.value = Penalties.NONE
        generateScr()
        timerJob = coroutineScope.launch {
            while (timerState == TimerState.GOING) {
                delay(10)
                _currentTime.value += 10
            }
        }
    }

    fun stopTimer() {
        _timerState.value = TimerState.INACTIVE
        hideEverything(false)
    }


    private fun timeToShow() : String {
        return when (timerState) {
            TimerState.INACTIVE -> {
                when (inspectionPenaltyState) {
                    Penalties.PLUS2 -> TimeFormat.millisToString(currentTime+2000)
                    Penalties.DNF -> "DNF"
                    Penalties.NONE -> TimeFormat.millisToString(currentTime)
                }
            }
            TimerState.INSPECTION -> {
                when (inspectionPenaltyState) {
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

    fun shortPressAction() {
        when (timerState) {
            TimerState.GOING -> {
                stopTimer()
                hideEverything(false)
            }
            TimerState.INACTIVE -> {
                hideEverything(true)
                if (INSPECTION_ON) {
                    startInspection()
                } else if (!DELAY_ON) {
                    startTimer()
                }
            }
            TimerState.INSPECTION -> {
                if (!DELAY_ON) {
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
                if (INSPECTION_ON) {
                    startInspection()
                    hideEverything(true)
                } else {
                    startTimer()
                    hideEverything(true)
                }
            }
            TimerState.INSPECTION -> startTimer()
        }
    }



}
