package com.example.cubetime.ui.screens.versus

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.cubetime.R
import com.example.cubetime.data.model.entities.Solve
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
    sessionDialogsViewModel: SessionDialogsViewModel,
) {

    val openDialog = remember { mutableStateOf(false) }
    val openDialogScramble = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        versusViewModel.init()
        versusViewModel.zeroScore()
        versusViewModel.timer1.clear()
        versusViewModel.timer2.clear()
    }


    var versusDialogState = remember { mutableStateOf(true) }
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
                        modifier = Modifier.padding(end = 10.dp, start = 10.dp),
                        text = versusViewModel.currentScramble)
                }
            }
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color =  MaterialTheme.colorScheme.onPrimaryContainer)) {
                        append(versusViewModel.scoreTop.toString())
                    }
                    append(":")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                        append(versusViewModel.scoreBottom.toString())
                    }
                },
                modifier = Modifier.padding(8.dp)
                    .graphicsLayer {
                        rotationX = 180f
                        rotationY = 180f
                    },
                fontSize = 25.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    navController.navigate("versusSolves") }) {
                    Text(stringResource(R.string.solves))
                }
                Button(onClick = {
                    navController.popBackStack()
                }) {
                    Text(stringResource(R.string.back_to_session))
                }
                Button(onClick = {
                    openDialog.value = true
                }) {
                    Text(stringResource(R.string.penalty))
                }
            }
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color =  MaterialTheme.colorScheme.onPrimaryContainer)) {
                        append(versusViewModel.scoreTop.toString())
                    }
                    append(":")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                        append(versusViewModel.scoreBottom.toString())
                    }
                },
                modifier = Modifier.padding(8.dp),
                fontSize = 25.sp

            )

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
                    modifier = Modifier.padding(end = 10.dp, start = 10.dp),
                    text = versusViewModel.currentScramble)
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