package com.example.cubetime.ui.screens.timer.dialogs

import androidx.compose.runtime.Composable
import com.example.cubetime.ui.shared.SharedViewModel


@Composable
fun TimerScreenDialogs(viewModel: SharedViewModel, closeDialog: () -> Unit, dialog: DialogTypes) {
    when (dialog) {
        DialogTypes.ADD_TIME -> {
            AddTimeDialog(
                onDismiss = closeDialog,
                action = {solveMillis, penalty ->
                    viewModel.timer.inputSolve(solveMillis, penalty)
                }
            )
        }
        DialogTypes.CUSTOM_SCRAMBLE -> {
            ScrambleDialog(
                onDismiss = closeDialog,
                action = { scramble -> viewModel.inputScramble(scramble) }
            )
        }
        DialogTypes.DELETE_SOLVE -> {
            DeleteSolveDialog(
                onDismiss = closeDialog,
                action = {viewModel.deleteLastSolve()}
            )

        }
        DialogTypes.ADD_COMMENT -> {
            CommentDialog(
                onDismiss = closeDialog,
                action = { }
            )
        }
        DialogTypes.NONE -> {

        }
    }
}