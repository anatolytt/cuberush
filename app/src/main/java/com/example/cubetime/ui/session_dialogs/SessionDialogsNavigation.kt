package com.example.cubetime.ui.session_dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.entities.Session
import com.example.cubetime.ui.appbar.AppBarViewModel
import com.example.cubetime.ui.shared.SharedViewModel

@Composable
fun SessionDialogsNavigation(
    dialogToShow: MutableState<DialogsState>,
    sessionDialogsViewModel: SessionDialogsViewModel,
    sessionOnCLick: (Session) -> Unit,
    event: Events? = null
) {
    when (dialogToShow.value) {
        DialogsState.SESSION -> {
            SessionDialog(
                onDismiss = { dialogToShow.value = DialogsState.NONE },
                onNext = { dialogToShow.value = DialogsState.EVENT },
                sessionDialogsViewModel = sessionDialogsViewModel,
                sessionOnCLick = sessionOnCLick,
                event = event
            )
        }

        DialogsState.EVENT -> {
            EventDialog(
                onDismiss = { dialogToShow.value = DialogsState.NONE },
                onBack = { dialogToShow.value = DialogsState.SESSION },
                sessionDialogsViewModel = sessionDialogsViewModel
            )
        }
        DialogsState.NONE -> {}
    }
}

enum class DialogsState { EVENT, SESSION, NONE }