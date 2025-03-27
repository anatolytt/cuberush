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
import androidx.compose.runtime.remember
import com.example.cubetime.ui.shared.SharedViewModel

@Composable
fun SolvesScreen(
    viewModel: SharedViewModel
){
    var chosenSolve by remember { mutableStateOf<Solve?>(null) }
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

    Button(
        onClick = {
            chosenSolve = Solve(
                result = 4987,
                event = Events.CUBE333,
                penalties = Penalties.NONE,
                date = "22-03-2025",
                scramble = "L' U' B2 D' L2 R2 D R2 U2 F2 R2 U' L' B L2 D' F D' U R",
                comment = "аофвыджаофвылаофвыжа\nfadfafsdf\nadsfdas\nadsfadsfasfsafadsfffsdfafasfasfadsjflads",
                reconstruction = "",
                isCustomScramble = true
            )
            scope.launch {
                sheetState.expand()
            }
        }
    ) { }
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
