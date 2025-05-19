package com.example.cubetime.ui.screens.versus.Timers

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.cubetime.ui.screens.timer.TimerController
import com.example.cubetime.ui.screens.timer.TimerState
import com.example.cubetime.ui.screens.timer.TimerViewModel
import com.example.cubetime.ui.screens.versus.VersusViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimerTop(
    modifier: Modifier,
    timer: TimerController,
    versusViewModel: VersusViewModel
) {

    var isLongPress by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val delayColorAnimation: Color by animateColorAsState(
        if (isLongPress && (timer.INSPECTION_ON == (timer.timerState == TimerState.INSPECTION)) && timer.timerState != TimerState.GOING) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    )

    val delaySizeAnimation: Int by animateIntAsState(
        if (isLongPress || timer.timerState != TimerState.INACTIVE) 65 else 50
    )
    val haptic = LocalHapticFeedback.current




    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        if (versusViewModel.counterBottom == 2 && versusViewModel.counterTop == 2) versusViewModel.zeroCounter()

                        if (versusViewModel.counterTop == 2) return@detectTapGestures

                        if (timer.timerState == TimerState.GOING) {
                            //когда таймер остановился i + 1
                            versusViewModel.addCounterTop()
                            Log.d("versusCounter", versusViewModel.counterTop.toString())

                            timer.stopTimer()
                        } else {

                            isLongPress = false
                            isPressed = false
                            val pressJob = coroutineScope.launch {

                                isPressed = true
                                delay(500)

                                isLongPress = true
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)


                            }
                            try {

                                awaitRelease()
                                isPressed = false
                                if (!timer.delayAfterStopOn) {
                                    if (!isLongPress) {
                                        timer.shortPressAction()
                                        //добавляем при запуске таймера i + 1
                                        versusViewModel.addCounterTop()
                                        Log.d(
                                            "versusCounter",
                                            versusViewModel.counterTop.toString()
                                        )

                                    } else {
                                        timer.longPressAction()
                                        //добавляем при запуске таймера i + 1
                                        versusViewModel.addCounterTop()
                                        Log.d(
                                            "versusCounter",
                                            versusViewModel.counterTop.toString()
                                        )

                                    }
                                }
                                pressJob.cancel()
                                isLongPress = false


                            } catch (_: GestureCancellationException) {

                            }
                        }


                    }
                )
            },
    ) {

        Text(
            text =
            if (isPressed && timer.timerState == TimerState.INACTIVE) {
                "0.00"
            } else {
                timer.currentTimeToShow
            },
            fontSize = delaySizeAnimation.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = delayColorAnimation
        )
    }


}




