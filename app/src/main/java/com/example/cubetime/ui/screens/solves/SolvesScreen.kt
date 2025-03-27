package com.example.cubetime.ui.screens.solves

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.utils.TimeFormat


@Composable
fun SolvesScreen(
    viewModel: SharedViewModel,
) {

    val solveList by remember { mutableStateOf(viewModel.solve) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Строка поиска")

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
        )
        {
            items(solveList.size) { index ->
                val solve = solveList[index]
                Card(
                    modifier = Modifier.height(60.dp)

                ) {
                    Text(text = TimeFormat.millisToString(solve.result),
                        modifier = Modifier.padding(5.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )

                }

            }
        }
    }

}
