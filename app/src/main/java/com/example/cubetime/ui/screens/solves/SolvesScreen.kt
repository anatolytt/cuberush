package com.example.cubetime.ui.screens.solves

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Solve
import com.example.cubetime.ui.screens.solves.dialogs.SolveBottomSheet
import com.example.cubetime.ui.screens.timer.TimerViewModel
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SolvesScreen(
    sharedViewModel: SharedViewModel,
    solvesViewModel: SolvesViewModel
) {
    var chosenSolve = solvesViewModel.chosenSolve
    val solveList by solvesViewModel.solvesList.collectAsState(initial = emptyList())


    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(Unit) {
        sharedViewModel.setSolvesDelete { solvesViewModel.deleteSelectedSolves() }
    }

    var scrollPosition by rememberSaveable { solvesViewModel.scrollPosition }
    val listState = rememberLazyGridState(
        initialFirstVisibleItemIndex = scrollPosition
    )
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            scrollPosition = listState.firstVisibleItemIndex
        }
    }

    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val includeScrambles by sharedViewModel.settingsManager.getPrintScrambles().collectAsState(
        true
    )
    if (chosenSolve != null) {
        SolveBottomSheet(
            onDismiss = {
                scope.launch {
                    sheetState.hide()
                    solvesViewModel.unchooseSolve()
                }
            },
            sheetState = sheetState,
            solve = chosenSolve!!,
            includeScramble = includeScrambles,
            solvesViewModel = solvesViewModel
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
            state = listState
        )
        {
            items(solveList, key = {it.id}, contentType = {"solve"}) { solve ->
                val isSelected = solvesViewModel.selectedSolveIds.contains(solve.id)

                ElevatedCard(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .combinedClickable(
                            onClick = {
                                if (sharedViewModel.deleteSolveAppBar) {
                                    solvesViewModel.enableDeleteMode(solve.id)
                                } else {
                                    solvesViewModel.chooseSolveById(solve.id)
                                }
                            },
                            onLongClick = {
                                solvesViewModel.enableDeleteMode(solve.id)
                                if (!sharedViewModel.deleteSolveAppBar) {
                                    sharedViewModel.changeAppBar()
                                }
                                // вибрация при выборе сборки
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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

