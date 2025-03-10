package com.example.cubetime.ui.appbar.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cubetime.ui.shared.SharedViewModel

@Composable
fun AppBarDialogNavigation(dialogToShow: MutableState<DialogsState>, viewModel: SharedViewModel) {
    when (dialogToShow.value) {
        DialogsState.SESSION -> {
            SessionDialog(
                onDismiss = { dialogToShow.value = DialogsState.NONE },
                onNext = { dialogToShow.value = DialogsState.EVENT }
            )
        }

        DialogsState.EVENT -> {
            EventDialog(
                onDismiss = { dialogToShow.value = DialogsState.NONE },
                onBack = { dialogToShow.value = DialogsState.SESSION },
                viewModel = viewModel
            )
        }
        DialogsState.NONE -> {}
    }
}

enum class DialogsState { EVENT, SESSION, NONE }