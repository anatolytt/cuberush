package com.example.cubetime.ui.screens.versus

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.cubetime.R
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.ui.screens.settings.Settings
import com.example.cubetime.ui.screens.timer.TimerViewModel
import com.example.cubetime.ui.screens.versus.Dialogs.PenaltyVersus
import com.example.cubetime.ui.screens.versus.Dialogs.ScrambleVersus
import com.example.cubetime.ui.screens.versus.Dialogs.createVersusDialog
import com.example.cubetime.ui.screens.versus.Timers.TimerBottom
import com.example.cubetime.ui.screens.versus.Timers.TimerTop
import com.example.cubetime.ui.session_dialogs.SessionDialogsViewModel
import com.example.cubetime.ui.shared.SharedViewModel
import kotlin.concurrent.timer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VersusScreen(
    viewModel: SharedViewModel,
    navController: NavController,
    versusViewModel: VersusViewModel,
    sessionDialogsViewModel: SessionDialogsViewModel
) {

    val openDialog = remember { mutableStateOf(false) }
    val openDialogScramble = remember { mutableStateOf(false) }
    var versusDialogState = remember { mutableStateOf(false) }

    val settings by viewModel.settingsManager.getTimerSettings().collectAsState(
        initial = Settings()
    )


    LaunchedEffect(Unit) {
        versusDialogState.value = !versusViewModel.matchCreated
        versusViewModel.updateTimerSettings(settings)
    }

    BackHandler {
        if (!versusViewModel.someTimerIsGoing && ! versusViewModel.someTimerFirstSolve) {
            versusViewModel.clear()
            navController.popBackStack()
        }
    }

    if (versusDialogState.value) {
        createVersusDialog(
            navController = navController,
            createMatch = { session1, session2 ->
                versusDialogState.value = false
                versusViewModel.setSessions(session1!!, session2!!)
            },
            sessionDialogsViewModel = sessionDialogsViewModel
        )
    }


    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        //Вверхний игнрок
        Column(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer {
                    rotationX = 180f
                    rotationY = 180f
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(){
                TimerTop(Modifier, versusViewModel.timer1, versusViewModel)
            }


        }


        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(modifier = Modifier.graphicsLayer {
                rotationX = 180f
                rotationY = 180f
            }) {
                if (versusViewModel.currentScramble.length >= 65) {
                    Button(
                        onClick = {
                            openDialogScramble.value = true
                        }, modifier = Modifier.padding(8.dp)
                    ) {
                        Text(stringResource(R.string.viewScrable))
                    }
                } else {
                    Text(
                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                        text = versusViewModel.currentScramble,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = {
                            if (!versusViewModel.someTimerIsGoing && ! versusViewModel.someTimerFirstSolve) {
                                versusViewModel.clear()
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSecondaryContainer)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrowback),
                            tint = Color.White,
                            contentDescription = ""
                        )
                    }

                    IconButton(
                        onClick = {
                            if (!versusViewModel.someTimerIsGoing && ! versusViewModel.someTimerFirstSolve) {
                                openDialog.value = true
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSecondaryContainer)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plustwo),
                            tint = Color.White,
                            contentDescription = ""
                        )
                    }
                }

                Column (
                    modifier = Modifier.width(85.dp)
                ){
                    Score(
                        playerScore = versusViewModel.scoreTop,
                        otherScore = versusViewModel.scoreBottom,
                        modifier = Modifier.rotate(180F).fillMaxWidth()
                    )
                    Spacer(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .height(20.dp)
                            .fillMaxWidth()

                    )
                    Score(
                        playerScore = versusViewModel.scoreBottom,
                        otherScore = versusViewModel.scoreTop
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer),

                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            if (!versusViewModel.someTimerIsGoing && ! versusViewModel.someTimerFirstSolve) {
                                navController.navigate("versusSolves")
                            }
                        },

                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            contentColor = Color.White
                        )

                    ) {
                        Text(stringResource(R.string.solves))
                    }
                }
            }



            if (versusViewModel.currentScramble.length >= 65) {
                Button(
                    onClick = {
                        openDialogScramble.value = true
                    }, modifier = Modifier.padding(8.dp)
                ) {

                    Text(stringResource(R.string.viewScrable))
                }
            } else {
                Text(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    text = versusViewModel.currentScramble,
                    textAlign = TextAlign.Center
                )

            }

        }

        // Нижний игрок
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerBottom(Modifier, versusViewModel.timer2, versusViewModel)

        }
    }
    if (openDialog.value) {
        PenaltyVersus(onDismissRequest = { openDialog.value = false },versusViewModel)
    }
    if (openDialogScramble.value) {
        ScrambleVersus(onDismissRequest = { openDialogScramble.value = false },versusViewModel)
    }
}

@Composable
fun Score(
    playerScore: Int,
    otherScore: Int,
    modifier: Modifier = Modifier
) {
    Card (modifier = Modifier
        .wrapContentSize(),
        shape = RectangleShape,
    ){
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )) {
                    append(playerScore.toString())
                }
                append(":")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                    append(otherScore.toString())
                }
            },
            modifier = modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 7.dp),
            textAlign = TextAlign.Center,
            fontSize = 25.sp

        )
    }

}