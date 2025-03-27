package com.example.cubetime.ui.screens.solves

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.cubetime.model.Events
import com.example.cubetime.model.Penalties
import com.example.cubetime.model.Solve
import com.example.cubetime.ui.screens.solves.dialogs.SolveBottomSheet
import com.example.cubetime.ui.shared.SharedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
}
