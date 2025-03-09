package com.example.cubetime.ui.screens.timer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cubetime.R
import com.example.cubetime.model.Events
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.launch

@Composable
fun TimerScreen() {
    val scrambler = Scrambler()
    val event = Events.CUBE333
    var scramble by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center

    ) {
        Timer()
    }

    LaunchedEffect(event) {
        scramble = scrambler.generateScramble(event)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            scramble.split("\n").forEach { line ->
                Text(
                    text = line,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(bottom = 5.dp, start = 10.dp, end = 10.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = {
                    coroutineScope.launch {
                        scramble = scrambler.generateScramble(event)
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.img_1),
                        contentDescription = "Generate scramble"
                    )
                }
            }
        }
    }
}



