package com.example.cubetime.ui.screens.versus.Dialogs

import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.cubetime.R
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.entities.Session
import com.example.cubetime.ui.screens.versus.VersusViewModel
import com.example.cubetime.ui.session_dialogs.DialogsState
import com.example.cubetime.ui.session_dialogs.SessionDialogsNavigation
import com.example.cubetime.ui.session_dialogs.SessionDialogsViewModel

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun createVersusDialog(
    navController: NavController,
    sessionDialogsViewModel: SessionDialogsViewModel,
    createMatch: (Session?, Session?) -> Unit
) {
    val session1 = remember { mutableStateOf<Session?>(null) }
    val session2 = remember { mutableStateOf<Session?>(null) }
    val chosenEvent = remember { mutableStateOf<Events>(Events.CUBE333) }

    val dropdownExpanded = remember { mutableStateOf(false) }


    Dialog(
        onDismissRequest = {}
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            ) {
            Column (
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrowback),
                        contentDescription = ""
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded.value,
                    onExpandedChange = {dropdownExpanded.value = !dropdownExpanded.value},
                    modifier = Modifier.width(200.dp)
                ) {
                    TextField(
                        value = stringResource(chosenEvent.value.getEventStringId()),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded.value,
                        onDismissRequest = {dropdownExpanded.value = false}
                    ) {
                        Events.entries.forEach { event ->
                            val name = stringResource(event.getEventStringId())
                            DropdownMenuItem(
                                text = {
                                    Text(name)
                                },
                                onClick = {
                                    chosenEvent.value = event
                                    dropdownExpanded.value = false
                                },
                            )
                        }
                    }

                }



                Spacer(modifier = Modifier.height(10.dp))

                Row() {
                    PlayerSessionBlock(
                        session = session1,
                        modifier = Modifier.weight(1F),
                        headlineText = stringResource(R.string.player) + " 1",
                        sessionDialogsViewModel = sessionDialogsViewModel,
                        event = chosenEvent.value
                    )

                    PlayerSessionBlock(
                        session = session2,
                        modifier = Modifier.weight(1F),
                        headlineText = stringResource(R.string.player) + " 2",
                        sessionDialogsViewModel = sessionDialogsViewModel,
                        event = chosenEvent.value
                    )

                }
                Spacer(modifier = Modifier.height(15.dp))
                Button(onClick = {createMatch(session1.value, session2.value)}) {
                    Text(stringResource(R.string.create_battle))
                }
            }
        }
    }
}

@Composable
fun PlayerSessionBlock(
    session: MutableState<Session?>,
    modifier: Modifier,
    headlineText: String,
    sessionDialogsViewModel: SessionDialogsViewModel,
    event: Events
) {
    val sessionDialogToShow =  remember { mutableStateOf(DialogsState.NONE) }
    SessionDialogsNavigation(
        dialogToShow = sessionDialogToShow,
        sessionDialogsViewModel = sessionDialogsViewModel,
        event = event,
        sessionOnCLick = { newSession ->
            session.value = newSession
        }
    )

    val noSession = stringResource(R.string.no_session)
    val sessionText = remember { mutableStateOf<String>(noSession) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = headlineText,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (session.value == null) {
            sessionText.value = noSession
        } else {
            sessionText.value = session.value!!.name
        }

        Text(
            text = sessionText.value,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))
        TextButton (
            onClick = { sessionDialogToShow.value = DialogsState.SESSION }
        ) {
            Text(stringResource(R.string.change_session))
        }
    }

}