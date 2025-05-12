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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cubetime.ui.screens.timer.TimerViewModel
import com.example.cubetime.ui.screens.versus.Dialogs.ModalTopSheet
import com.example.cubetime.ui.screens.versus.Dialogs.PenaltyVersus
import com.example.cubetime.ui.screens.versus.Timers.TimerBottom
import com.example.cubetime.ui.screens.versus.Timers.TimerTop
import com.example.cubetime.ui.shared.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VersusScreen(
    viewModel: SharedViewModel, navController: NavController, timerViewModel: TimerViewModel
) {

    val openDialog = remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    var showSheetTop = remember { mutableStateOf(false) }

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
            TimerTop(Modifier.padding(100.dp), timerViewModel)
        }


        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(modifier = Modifier.graphicsLayer {
                    rotationX = 180f
                    rotationY = 180f
                }) {
                if(timerViewModel.currentScramble.length >= 60)
                {
                    Button(
                        onClick = {
                            showSheetTop.value = true
                        }, modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Показать скрамбл")
                    }
                }
                else{
                    Text(text = timerViewModel.currentScramble)
                }
            }
            Text("0:0", modifier = Modifier.padding(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {}) {
                    Text("Сессия")
                }
                Button(onClick = {
                    navController.popBackStack()
                    viewModel.changeVersusVisibility()
                }) {
                    Text("Назад")
                }
                Button(onClick = {
                        openDialog.value = true

                }) {
                    Text("Штраф")
                }
            }
            Text("0:0", modifier = Modifier.padding(8.dp))
            if(timerViewModel.currentScramble.length >= 60)
            {
                Button(
                    onClick = {}, modifier = Modifier.padding(8.dp)
                ) {
                    //при нажатии на кнопку будет открывать шитс панель
                    Text("Показать скрамбл")
                }
            }
            else
            {
                Text(text = timerViewModel.currentScramble)
            }

        }


        // Нижний игрок
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerBottom(Modifier.padding(100.dp), timerViewModel)
        }
    }
    if(openDialog.value)
    {
        PenaltyVersus (onDismissRequest = {openDialog.value = false})
    }
    if (showSheetTop.value)
    {
        ModalTopSheet(
            sheetState = sheetState,
            onDismiss = { showSheetTop.value = false }
        )
    }
}