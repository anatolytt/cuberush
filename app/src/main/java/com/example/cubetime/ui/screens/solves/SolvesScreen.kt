package com.example.cubetime.ui.screens.solves

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cubetime.R
import com.example.cubetime.ui.screens.solves.dialogs.DropSortedMenu
import com.example.cubetime.ui.screens.solves.dialogs.SolveBottomSheet
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.ui.theme.backgroundDark
import com.example.cubetime.utils.TimeFormat
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
        solvesViewModel.changeMinMaxSearch("")
        solvesViewModel.saveMode()


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
        val semicircleShape = RoundedCornerShape(50.dp)
        val textField = remember { mutableStateOf("") }
        var focused by remember { mutableStateOf(false) }
        TextField(
            value = textField.value,
            placeholder = { Text(stringResource(R.string.searchTime)) },
            onValueChange = { searchText ->
                var flag = 1
                var dotCount = 0
                var coloncount = 0
                searchText.forEach { char ->
                    when {
                        char.isDigit() -> {}
                        char == ':' -> {
                            coloncount++
                            if (coloncount > 1) flag = 0
                        }
                        char == '.' -> {
                            dotCount++
                            if (dotCount > 1) flag = 0
                        }
                        else -> flag = 0
                    }
                }
                if (searchText.startsWith(".")) {
                    flag = 0
                }
                if (searchText.startsWith(":")) {
                    flag = 0
                }

                if (flag == 1 || searchText.isEmpty() || (searchText.contains(":") &&
                            searchText.contains(".") && searchText.length > 7)
                ) {

                    textField.value = searchText

                    if (searchText.isNotEmpty()) {
                        solvesViewModel.changeMinMaxSearch(searchText)

                    } else {
                        solvesViewModel.changeMinMaxSearch("")
                    }
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(55.dp)
                .clip(semicircleShape)
                .background(Color.LightGray, CircleShape)
                .onFocusChanged { focusState ->
                    focused = focusState.isFocused
                },
            trailingIcon =
            {
                if (textField.value.isEmpty()) {
                    DropSortedMenu(solvesViewModel)
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
        )


        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            state = listState
        )
        {
            items(solveList, key = { it.id }, contentType = { "solve" }) { solve ->
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

