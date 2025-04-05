package com.example.cubetime.ui.appbar.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.cubetime.ui.appbar.AppBarViewModel
import com.example.cubetime.ui.appbar.dialogs.sessionDialog.SessionDialog
import com.example.cubetime.ui.shared.SharedViewModel

@Composable
fun AppBarDialogNavigation(dialogToShow: MutableState<DialogsState>, viewModel: SharedViewModel, appBarViewModel: AppBarViewModel) {
    when (dialogToShow.value) {
        DialogsState.SESSION -> {
            SessionDialog(
                onDismiss = { dialogToShow.value = DialogsState.NONE },
                onNext = { dialogToShow.value = DialogsState.EVENT },
                viewModel = viewModel,
                appBarViewModel = appBarViewModel
            )
        }

        DialogsState.EVENT -> {
            EventDialog(
                onDismiss = { dialogToShow.value = DialogsState.NONE },
                onBack = { dialogToShow.value = DialogsState.SESSION },
                viewModel = viewModel,
                appBarViewModel = appBarViewModel
            )
        }
        DialogsState.NONE -> {}
    }
}

enum class DialogsState { EVENT, SESSION, NONE }