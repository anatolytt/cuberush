package com.example.cubetime.ui.screens.solves

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cubetime.model.Solve
import com.example.cubetime.ui.screens.solves.dialogs.SolveBottomSheet
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SolvesScreen(
    viewModel: SharedViewModel
){
    var chosenSolve by remember { mutableStateOf<Solve?>(null) }
    val solveList = viewModel.solve


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
        Text("поле поиска тут будет")


        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
        )
        {
            val reversedList = solveList.reversed()
            items(reversedList.size) { index ->
                val solve = reversedList[index]
                val isSelected = viewModel.selectedSolveIds.contains(solve.id)

                solve.id  = ""+index
                ElevatedCard(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(5.dp)
                        //ripple эффект
                        .clip(RoundedCornerShape(12.dp))
                        .combinedClickable(
                            onClick = {
                                if (viewModel.longPressMode) {
                                    viewModel.addIDInSolvesLits(solve.id)
                                } else {
                                    chosenSolve = solve
                                }
                            },
                            onLongClick = {
                                viewModel.enableDeleteMode(solve.id)
                            }
                        )
                        .border(
                            width = if (isSelected) 2.dp else 0.dp,
                            color = if (isSelected) Color.Black else Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        ),
                ) {
                    Text(

                        text = TimeFormat.millisToString(solve.result, solve.penalties),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 13.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 19.sp
                    )

                }

            }
        }
    }

}
