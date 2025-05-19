package com.example.cubetime.ui.screens.timer



import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.cubetime.ui.shared.SharedViewModel
import androidx.compose.foundation.layout.*


import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.cubetime.R

import com.example.cubetime.data.model.Penalties
import com.example.cubetime.ui.screens.timer.dialogs.DialogTypes
import com.example.cubetime.ui.screens.timer.dialogs.TimerScreenDialogs
import com.example.cubetime.ui.screens.settings.Settings
import com.example.cubetime.ui.screens.statistics.CurrentStatsUI
import com.example.cubetime.ui.shared.ScrambleImage


@Composable
fun TimerScreen(
    viewModel: SharedViewModel,
    timerViewModel: TimerViewModel) {
    val settings by viewModel.settingsManager.getTimerSettings().collectAsState(
        initial = Settings()
    )

    LaunchedEffect(Unit) {
        timerViewModel.init(
            {hide -> viewModel.hideEverything(hide)},
            {state -> viewModel.setGeneratingState(state)}
        )
    }
    val timer = timerViewModel.timer
    timerViewModel.updateTimerSettings(settings)


    val configuration = LocalConfiguration.current
    var currentDialog by remember { mutableStateOf(DialogTypes.NONE) }
    val hideAnimation by animateFloatAsState(   // Анимация для скрытия элементов при запуске таймера
        targetValue = if (viewModel.everythingHidden) 0f else 1f,
        animationSpec = tween(durationMillis = 100)
    )
    var scrambleIsBig by remember { mutableStateOf(false) }
    val scrambleSizeAnimation by animateIntAsState(
        targetValue = if (scrambleIsBig) 350 else 115,
        animationSpec = tween(durationMillis = 500)
    )

    val screenHeight = configuration.screenHeightDp
    val scrambleMoveAnimation by animateDpAsState(
        targetValue = if (scrambleIsBig) (-(screenHeight/4)).dp else 0.dp,
        animationSpec = tween(durationMillis = 300)
    )

    // Переопределение действия при свайпе влево или нажатии на кнопку назад
    if (timer.timerState != TimerState.INACTIVE) {
        BackHandler { timer.stopAndDelete() }
    }

    val averages by timerViewModel.averages.collectAsState(initial = CurrentStatsUI())
    val bestAverages by timerViewModel.PBs.collectAsState(initial = CurrentStatsUI())
    val solvesCounter by timerViewModel.solvesCounter.collectAsState(initial = 0)


    TimerScreenDialogs(
        dialog = currentDialog,
        viewModel = viewModel,
        closeDialog = {currentDialog = DialogTypes.NONE},
        timerViewModel = timerViewModel
    )
    Log.d("TimerScreen", viewModel.scrambleIsGenerating.toString())

    // Скрамбл и кнопки для управления скрамблом
    Box(modifier = Modifier
        .padding(12.dp)
        .zIndex(1F),

        contentAlignment = Alignment.BottomCenter) {

        Box(
            modifier = Modifier.fillMaxSize().zIndex(5F),
            contentAlignment = Alignment.TopCenter
        ) {
            if (!viewModel.scrambleIsGenerating) {
            Column(
                modifier = Modifier
                    .alpha(hideAnimation),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = timerViewModel.currentScramble,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,

                    modifier = Modifier
                        .padding(bottom = 5.dp)
                )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 5.dp),
                        horizontalArrangement = Arrangement.Start

                    ) {
                        Row {
                            //карандаш
                            IconButton(onClick = {
                                currentDialog = DialogTypes.CUSTOM_SCRAMBLE
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.pencil),
                                    contentDescription = "Custom scramble"
                                )
                            }
                            //ввод времени
                            IconButton(onClick = {
                                currentDialog = DialogTypes.ADD_TIME
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.timeradd),
                                    contentDescription = "Input time"
                                )
                            }
                        }
                        //веертушка для срамбла
                        IconButton(onClick = {
                            timerViewModel.updateCurrentScramble()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrowreload),
                                contentDescription = "Generate new scramble"
                            )
                        }

                    }
                    }
                } else {
                    Column {
                        Text(
                            text = "Scramble is generating",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,

                            modifier = Modifier
                                .padding(bottom = 5.dp)
                        )
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

            }
        }

        Timer(
            hideEverything = ({ h: Boolean -> viewModel.hideEverything(h) }),
            modifier = Modifier.composed {
                if (viewModel.everythingHidden) {
                    Modifier.zIndex(15F)
                } else {
                    Modifier.zIndex(0F)
                }
            },
            timerViewModel = timerViewModel
        )

        // Статистика
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1F)
                .alpha(hideAnimation),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedCard(
                    modifier = Modifier.padding(bottom = 5.dp, end = 7.dp, start = 7.dp),
                    colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = Color.White,
                        disabledContentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(
                        modifier = Modifier
                            .width(110.dp)
                            .padding(9.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "PB: " + bestAverages.single,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(1.dp),
                            fontWeight = FontWeight.Bold,

                        )
                        Text(
                            text = "PB Ao5: " + bestAverages.ao5,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(1.dp)
                        )
                        Text(
                            text = "PB Ao12 " + bestAverages.ao12,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(1.dp),
                            fontWeight = FontWeight.Bold,

                        )
                        Text(
                            text = stringResource(R.string.solves_amount) + ": " + solvesCounter,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(1.dp),
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Mean: " + averages.mean,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(1.dp),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                OutlinedCard(
                    modifier = Modifier.padding(bottom = 5.dp, end = 7.dp, start = 7.dp),
                    colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = Color.White,
                        disabledContentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(
                        modifier = Modifier
                            .width(110.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "Ao5: " + averages.ao5,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(1.dp)
                        )
                        Text(
                            text = "Ao12: " + averages.ao12,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(1.dp)
                        )
                        Text(
                            text = "Ao25: " + averages.ao25,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(1.dp)
                        )
                        Text(
                            text = "Ao50: " + averages.ao50,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(1.dp)
                        )
                        Text(
                            text = "Ao100: " + averages.ao100,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(3.dp)
                        )
                    }
                }
            }
        }

        // Пустой Box для обработки нажатия на экран для уменьшения размера скрамбла
        Box(modifier = Modifier
            .fillMaxSize()
            .composed {
                if (scrambleIsBig) {
                    Modifier
                        .zIndex(11F)
                        .pointerInput (Unit) {
                            detectTapGestures { scrambleIsBig = false }
                        }
                } else {
                    Modifier.zIndex(0F)
                }
        })

        // Картинка скрамбла
        if (!viewModel.scrambleIsGenerating) {
            Box(modifier = Modifier
                .wrapContentSize()
                .alpha(hideAnimation)
                .zIndex(10F)
                .pointerInput(Unit) {
                    if (!scrambleIsBig) {
                        detectTapGestures { scrambleIsBig = true }
                    }
                }
                .offset(y = scrambleMoveAnimation),
                contentAlignment = Alignment.BottomCenter) {
                ScrambleImage(
                    svgString = timerViewModel.currentImage,
                    sizeDp = scrambleSizeAnimation.dp
                )
            }
        } else {
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                CircularProgressIndicator()
            }
        }


        // Кнопки для управления прошлой сборкой
        if (!timer.isFirstSolve) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(hideAnimation)
                    .padding(top = 150.dp)
                    .zIndex(1F),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    //кнопка удаления результата
                    IconButton(onClick = {currentDialog = DialogTypes.DELETE_SOLVE}) {
                        Icon(
                            painter = painterResource(id = R.drawable.cross),
                            contentDescription = "Generate scramble"
                        )
                    }

                    //кнопка для dnf
                    IconButton(onClick = {
                        timer.changePenalty(Penalties.DNF)
                        timerViewModel.updatePenalty(id=0, timer.penaltyState)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.dnf),
                            contentDescription = "Generate scramble"
                        )
                    }

                    //кнопка добавление +2 ко времени
                    IconButton(onClick = {
                        timer.changePenalty(Penalties.PLUS2)
                        timerViewModel.updatePenalty(id=0, timer.penaltyState)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.plustwo),
                            contentDescription = "Generate scramble"
                        )
                    }
                    //кнопка добавления коментария сборки
                    IconButton(onClick = {currentDialog = DialogTypes.ADD_COMMENT}
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.dnf),
                            contentDescription = "Generate scramble"
                        )
                    }
                }

            }
        }

    }
}



