package com.example.cubetime.ui.screens.timer

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.FontScaling
import androidx.compose.ui.unit.sp
import com.example.cubetime.ui.shared.SharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Timer(hideEverything: (Boolean) -> Unit, modifier: Modifier,
          viewModel: SharedViewModel) {
    val timer by remember { mutableStateOf(TimerController(hideEverything,
                            {viewModel.generateNewScrambleAndImage()}))}

    var isLongPress by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val delayColorAnimation: Color by animateColorAsState(
        if (isLongPress && (timer.INSPECTION_ON == (timer.timerState == TimerState.INSPECTION)) && timer.timerState != TimerState.GOING)  {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    )

    val delaySizeAnimation: Int by animateIntAsState(
        if (isLongPress || timer.timerState != TimerState.INACTIVE) 110 else 80
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        if (timer.timerState == TimerState.GOING) {
                            timer.stopTimer()
                        }
                        else {
                            isLongPress = false
                            val pressJob = coroutineScope.launch {
                                delay(500)
                                isLongPress = true
                            }
                            try {
                                awaitRelease()
                                if (!isLongPress) {
                                    timer.shortPressAction()
                                } else {
                                    timer.longPressAction()
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
            text = timer.currentTimeToShow,
            fontSize = delaySizeAnimation.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = delayColorAnimation
        )
    }

}




