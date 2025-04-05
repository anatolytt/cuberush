package com.example.cubetime.ui.screens.timer



import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cubetime.R

import com.example.cubetime.data.model.Penalties
import com.example.cubetime.ui.screens.timer.dialogs.DialogTypes
import com.example.cubetime.ui.screens.timer.dialogs.TimerScreenDialogs
import com.example.cubetime.ui.settings.SettingsDataManager
import com.example.cubetime.ui.settings.TimerSettings
import com.example.cubetime.ui.shared.ScrambleImage


@Composable
fun TimerScreen(viewModel: SharedViewModel) {
    val settingsDataManager = SettingsDataManager(LocalContext.current)
    val timerSettings by settingsDataManager.getTimerSettings().collectAsState(
        initial = TimerSettings(false, false, false)
    )

    val timerViewModel : TimerViewModel = viewModel()
    LaunchedEffect(Unit) {
        timerViewModel.init(timerSettings, {hide -> viewModel.hideEverything(hide)})
    }
    val timer = timerViewModel.timer


    val configuration = LocalConfiguration.current
    var currentDialog by remember { mutableStateOf(DialogTypes.NONE) }
    val hideAnimation by animateFloatAsState(   // Анимация для скрытия элементов при запуске таймера
        targetValue = if (viewModel.everythingHidden) 0f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    var scrambleIsBig by remember { mutableStateOf(false) }
    val scrambleSizeAnimation by animateIntAsState(
        targetValue = if (scrambleIsBig) 350 else 130,
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


    TimerScreenDialogs(
        dialog = currentDialog,
        viewModel = viewModel,
        closeDialog = {currentDialog = DialogTypes.NONE},
        timerViewModel = timerViewModel
    )

    // Скрамбл и кнопки для управления скрамблом
    Box(modifier = Modifier
        .padding(12.dp)
        .zIndex(1F),

        contentAlignment = Alignment.BottomCenter) {

        Box(
            modifier = Modifier.fillMaxSize().zIndex(5F),
            contentAlignment = Alignment.TopCenter
        ) {
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
                        IconButton(onClick = {currentDialog = DialogTypes.ADD_TIME
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.dnf),
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
                Card(
                    modifier = Modifier.padding(bottom = 5.dp, end = 10.dp, start = 10.dp),
                    elevation = CardDefaults.elevatedCardElevation(2.dp),
                    shape = CardDefaults.shape
                ) {
                    Text(
                        text = "Разброс:8.77\nСреднее:12 \nЛучшее:12\nК-во:21",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(7.dp)
                    )
                }
                Card(
                    modifier = Modifier.padding(bottom = 5.dp, end = 10.dp, start = 10.dp),
                    shape = CardDefaults.shape,
                    elevation = CardDefaults.elevatedCardElevation(2.dp)
                ) {
                    Text(
                        text = "Ao5: 2.37\nAo12: 19.32\nAo50: 2.37\nAo100: 1:22.12",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(7.dp)
                    )
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
        Box(modifier = Modifier
            .wrapContentSize()
            .alpha(hideAnimation)
            .zIndex(10F)
            .pointerInput (Unit) {
                if (!scrambleIsBig) {
                    detectTapGestures { scrambleIsBig = true }
                }
            }
            .offset(y=scrambleMoveAnimation),
            contentAlignment = Alignment.BottomCenter) {
            ScrambleImage(
                svgString = timerViewModel.currentImage,
                sizeDp = scrambleSizeAnimation.dp
            )
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
                    IconButton(onClick = { timer.changePenalty(Penalties.DNF) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.dnf),
                            contentDescription = "Generate scramble"
                        )
                    }

                    //кнопка добавление +2 ко времени
                    IconButton(onClick = { timer.changePenalty(Penalties.PLUS2) }) {
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



