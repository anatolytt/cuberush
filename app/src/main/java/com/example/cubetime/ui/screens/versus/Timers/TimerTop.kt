package com.example.cubetime.ui.screens.versus.Timers

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
import com.example.cubetime.ui.screens.timer.TimerState
import com.example.cubetime.ui.screens.timer.TimerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimerTop(
          modifier: Modifier,
          timerViewModel: TimerViewModel
) 
{
    val timer = timerViewModel.timer1
    var isLongPress by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val delayColorAnimation: Color by animateColorAsState(
        if (isLongPress && (timer.INSPECTION_ON == (timer.timerState == TimerState.INSPECTION)) && timer.timerState != TimerState.GOING)  {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onBackground
        }

    )

    val delaySizeAnimation: Int by animateIntAsState(
        if (isLongPress || timer.timerState != TimerState.INACTIVE) 75 else 65
    )
    val haptic = LocalHapticFeedback.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        if (timer.timerState == TimerState.GOING) {
                            timer.stopTimer()
                        }
                        else {
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
                                    } else {
                                        timer.longPressAction()
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
                    }
                ,
                fontSize = delaySizeAnimation.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = delayColorAnimation)
    }

}




