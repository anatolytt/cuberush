package com.example.cubetime.ui.screens.timer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.model.Penalties
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

//class TimerViewModel : ViewModel() {
//    // напоминание для себя: добавить анимацию изменения времени инспекции
//    private var _currentTimeToShow = MutableStateFlow("0.00")
//    val currentTimeToShow = _currentTimeToShow.asStateFlow()
//
//    private val _currentTime = MutableStateFlow(0.00)
//    val currentTime = _currentTime.asStateFlow()
//
//    private val _currentTimerState = MutableStateFlow(TimerState.GOING)
//    val currentTimerState = _currentTimerState.asStateFlow()
//
//
//    private val _inspectionPenaltyState = MutableStateFlow(Penalties.NONE)
//    val inspectionPenaltyState = _inspectionPenaltyState.asStateFlow()
//
//    private val _timerState = MutableStateFlow<TimerState>(TimerState.INACTIVE)
//    val timerState = _timerState.asStateFlow()
//
//    val inspectionLength = 15000    // мс
//
//    var timerJob: Job? = null
//
//    fun startDelay() {
//        timerJob?.cancel()
//        _currentTime.value = 0.00
//        _currentTimeToShow.value = "0.00"
//        timerJob = viewModelScope.launch {
//            while (currentTimerState.value == TimerState.DELAY) {
//
//            }
//        }
//    }
//
//    fun startInspection() {
//        timerJob?.cancel()
//        _currentTime.value = 15000.00
//        _currentTimeToShow.value = "15"
//        timerJob = viewModelScope.launch {
//            while (currentTimerState.value == TimerState.INSPECTION) {
//                delay(1000)
//                _currentTime.value -= 1000
//                _currentTimeToShow.value = (currentTime.value.roundToInt().toString())
//
//                if (currentTime == 0.00) {
//                    inspectionPenaltyState.value = Penalties.DNF
//                    currentTime = -1.00
//                    currentTimeToShow = "DNF"
//                    currentTimerState.value = mutableStateOf(TimerState.INACTIVE).value
//                }
//
//                if (currentTime == -2000.00) {
//                    inspectionPenaltyState.value = Penalties.PLUS2
//                }
//            }
//        }
//
//    }
//
//    fun startTimer() {
//        timerJob?.cancel()
//        currentTime = 0.00
//        currentTimeToShow = "0.00"
//        timerJob = viewModelScope.launch {
//            while (currentTimerState.value == TimerState.GOING) {
//                delay(10)
//                currentTime += 10
//                currentTimeToShow = ((currentTime / 1000).toString())
//
//            }
//        }
//    }
//}
