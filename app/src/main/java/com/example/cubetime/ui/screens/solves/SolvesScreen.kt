package com.example.cubetime.ui.screens.solves

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cubetime.model.Solve
import com.example.cubetime.ui.screens.solves.dialogs.SolveBottomSheet
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolvesScreen(
    viewModel: SharedViewModel
){
    var chosenSolve by remember { mutableStateOf<Solve?>(null) }
    val solveList by remember { mutableStateOf(viewModel.solve) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val scope = rememberCoroutineScope()

    if (chosenSolve != null) {
        SolveBottomSheet(
            onDismiss = {
                scope.launch {
                    sheetState.hide()
                    chosenSolve = null
                }
                        },
            sheetState = sheetState,
            solve = chosenSolve!!,
            viewModel = viewModel,
        )
    }

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
                ElevatedCard(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(5.dp)
                        .clickable {
                            chosenSolve = solve
                        },


                ) {
                    Text(
                        text = TimeFormat.millisToString(solve.result, solve.penalties),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        textAlign = TextAlign.Center,
                    )

                }

            }
        }
    }

}
