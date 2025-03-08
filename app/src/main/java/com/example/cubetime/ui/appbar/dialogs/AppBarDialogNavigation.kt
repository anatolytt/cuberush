package com.example.cubetime.ui.appbar.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun AppBarDialogNavigation(dialogToShow: MutableState<DialogsState>) {
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
                onBack = { dialogToShow.value = DialogsState.SESSION }
            )
        }
        DialogsState.NONE -> {}
    }
}

enum class DialogsState { EVENT, SESSION, NONE }