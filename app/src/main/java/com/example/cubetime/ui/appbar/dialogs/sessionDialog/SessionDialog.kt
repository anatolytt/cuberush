package com.example.cubetime.ui.appbar.dialogs.sessionDialog

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cubetime.R
import com.example.cubetime.data.model.Session
import com.example.cubetime.ui.appbar.AppBarViewModel
import com.example.cubetime.ui.shared.SharedViewModel
import kotlin.coroutines.CoroutineContext


@Composable
fun SessionDialog(
    onDismiss : () -> Unit,
    onNext: () -> Unit,
    viewModel: SharedViewModel,
    appBarViewModel: AppBarViewModel) {

    val context = LocalContext.current

    val sessionsList = appBarViewModel.sessionsList.collectAsState(initial = emptyList())

    var showSessionDialog by remember { mutableStateOf(false) }
    var sessionToDelete by remember { mutableStateOf<Session?>(null) }

    var showEditSessionDialog by remember { mutableStateOf(false) }
    var sessionToEdit by remember { mutableStateOf<Session?>(null) }
    var sessionToEditId by remember { mutableStateOf<Int?>(null) }

    var sessionIdWithMenuOpen by remember { mutableStateOf<Int?>(null) }

    if (showSessionDialog) {
        sessionIdWithMenuOpen = null
        DeleteSessionDialog(
            onDismiss = {showSessionDialog = false},
            action = {
                appBarViewModel.deleteSession(sessionToDelete!!)
                sessionToDelete = null
            }
        )
    }

    if (showEditSessionDialog) {
        sessionIdWithMenuOpen = null
        EditSessionDialog(
            onDismiss = { showEditSessionDialog = false },
            action = { newName ->
                appBarViewModel.renameSession(sessionToEdit!!, sessionToEditId!!, newName)
                sessionToEdit = null
                sessionToEditId = null
            }
        )
    }


    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically


                ) {
                    Text (
                        text= stringResource(R.string.select_session),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.align(Alignment.CenterVertically)

                    )
                    FilledTonalButton(
                        onClick = { onNext() },
                        Modifier.padding(1.dp)
                    ) {
                        Text (
                            text = stringResource(R.string.create_session),
                            fontSize = 10.sp
                        )
                    }

                }
                Spacer(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceDim)
                        .fillMaxWidth()
                        .height(3.dp)
                )
                Spacer(
                    modifier = Modifier.fillMaxWidth().height(10.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    itemsIndexed(
                        items = sessionsList.value,
                        key = {index, session -> session.name}) {index, session  ->
                        SwipeSessionItem(
                            deleteAction = {
                                if (sessionsList.value.size == 1) {
                                    sessionIdWithMenuOpen = null
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.last_session_delete_toast),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    sessionToDelete = session
                                    showSessionDialog = true
                                }
                                           },
                            editAction = {
                                sessionToEdit = session
                                sessionToEditId = index
                                showEditSessionDialog = true
                            },
                            onExpanded = { sessionIdWithMenuOpen = index },
                            isShown = (sessionIdWithMenuOpen == index),
                            onCollapsed = { sessionIdWithMenuOpen = null}
                        ) {
                            Column () {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { appBarViewModel.switchSessions(session.name) }
                                        .padding(vertical = 10.dp, horizontal = 22.dp),
                                    verticalAlignment = Alignment.CenterVertically,

                                    ) {
                                    Icon(
                                        painter = painterResource(session.event.getIconDrawableId()),
                                        contentDescription = "sessionEventIcon",
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )
                                    Text(
                                        text = session.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 35.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

