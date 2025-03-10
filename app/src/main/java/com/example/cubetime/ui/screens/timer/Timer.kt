
//package com.example.cubetime.ui.screens.timer
//
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//@Composable
//fun Timer() {
//    val INSPECTION_ON = false
//    val DURATION = 1000
//    val SIZE_K = 1            // коэфицент изменения размера текста таймера
//    val viewModel : TimerViewModel = viewModel()
//
//    Text(
//        text = viewModel.currentTimeToShow,
//        modifier = Modifier.fillMaxSize(),
//        )
//    LaunchedEffect(Unit) { viewModel.startTimer() }
//
//
//}

package com.example.cubetime.ui.screens.timer

import android.widget.TextView
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Timer

@Composable
fun Timer(){
    Text(
        text = "0.00",
        fontSize = 100.sp,
    )
}


