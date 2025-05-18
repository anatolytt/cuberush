package com.example.cubetime.ui.screens.versus.Dialogs

import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import com.example.cubetime.ui.screens.solves.dialogs.MyDivider
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
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrowback),
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded.value,
                    onExpandedChange = {dropdownExpanded.value = !dropdownExpanded.value},
                ) {
                    TextField(
                        value = stringResource(chosenEvent.value.getEventStringId()),
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded.value)
                        },
                        label = { Text(stringResource(R.string.event)) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded.value,
                        onDismissRequest = {dropdownExpanded.value = false},
                        modifier = Modifier.height(350.dp)
                    ) {
                        Events.entries.forEach { event ->
                            val name = stringResource(event.getEventStringId())
                            DropdownMenuItem(
                                text = {
                                    Text(name)
                                },
                                onClick = {
                                    chosenEvent.value = event
                                    session1.value = null
                                    session2.value = null
                                    dropdownExpanded.value = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }

                }



                Spacer(modifier = Modifier.height(10.dp))

                Row() {
                    PlayerSessionBlock(
                        session = session1,
                        modifier = Modifier.weight(1F),
                        headlineText = stringResource(R.string.cuber) + " 1",
                        sessionDialogsViewModel = sessionDialogsViewModel,
                        event = chosenEvent.value
                    )

                    PlayerSessionBlock(
                        session = session2,
                        modifier = Modifier.weight(1F),
                        headlineText = stringResource(R.string.cuber) + " 2",
                        sessionDialogsViewModel = sessionDialogsViewModel,
                        event = chosenEvent.value
                    )

                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { createMatch(session1.value, session2.value )},
                    modifier = Modifier.fillMaxWidth()
                    ) {
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
    if (session.value == null) {
        sessionText.value = noSession
    } else {
        sessionText.value = session.value!!.name
    }

    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = headlineText,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = sessionText.value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
            onClick = { sessionDialogToShow.value = DialogsState.SESSION }
        ) {
            Text(
                text = stringResource(R.string.change_session),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }

}