package com.example.cubetime.ui.screens.timer.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.cubetime.ui.screens.timer.TimerViewModel
import com.example.cubetime.ui.shared.SharedViewModel


@Composable
fun TimerScreenDialogs(viewModel: SharedViewModel,
                       closeDialog: () -> Unit, dialog: DialogTypes,
                       timerViewModel: TimerViewModel) {
    when (dialog) {
        DialogTypes.ADD_TIME -> {
            AddTimeDialog(
                onDismiss = closeDialog,
                action = {solveMillis, penalty ->
                    timerViewModel.timer.inputSolve(solveMillis, penalty)
                }
            )
        }
        DialogTypes.CUSTOM_SCRAMBLE -> {
            ScrambleDialog(
                onDismiss = closeDialog,
                action = { scramble -> timerViewModel.inputScramble(scramble) }
            )
        }
        DialogTypes.DELETE_SOLVE -> {
            DeleteSolveDialog(
                onDismiss = closeDialog,
                action = {timerViewModel.deleteLastSolve()}
            )

        }
        DialogTypes.ADD_COMMENT -> {
            CommentDialog(
                onDismiss = closeDialog,
                action = {

                }
            )
        }
        DialogTypes.NONE -> {

        }
    }
}