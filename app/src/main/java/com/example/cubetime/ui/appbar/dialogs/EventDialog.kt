package com.example.cubetime.ui.appbar.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cubetime.R
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.EventDialog
import com.example.cubetime.ui.appbar.AppBarViewModel
import com.example.cubetime.ui.appbar.dialogs.sessionDialog.SessionTextField
import com.example.cubetime.ui.shared.SharedViewModel
import kotlinx.coroutines.coroutineScope

@Composable
fun EventDialog(
    onDismiss: () -> Unit,
    onBack: () -> Unit,
    viewModel: SharedViewModel,
    appBarViewModel: AppBarViewModel
) {
    var text = remember { mutableStateOf("") }
    var selectEvents by remember { mutableStateOf(Events.CUBE333) }
    val sessionsList = appBarViewModel.sessionsList.collectAsState(initial = emptyList())

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = Dp.Unspecified, max = 900.dp)
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(15.dp)
            ) {

                //ВВОД НАЗВАНИЯ СЕССИИ ( name)
                SessionTextField(text)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.spacedBy(5.dp)

                )
                {
                    val eventsList = Events.entries
                    items(eventsList.size) { index ->
                        val event = eventsList[index]
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { selectEvents = event }

                                .background(
                                    if (event == selectEvents)
                                        MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(15.dp)
                                )


                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .fillMaxWidth()
                                    .size(40.dp),

                                painter = painterResource(event.getIconDrawableId()),
                                contentDescription = stringResource(event.getEventStringId())
                            )
                            Text(
                                text = stringResource(event.getEventStringId()),
                                fontSize = 11.sp
                            )
                        }

                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, end = 10.dp, start = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween

                )
                {
                    Button(
                        onClick = { onBack() },
                    ) {
                        Text(
                            text = stringResource(R.string.back_to_session)
                        )
                    }

                    //КНОПКА СОЗДАНИЯ СЕССИЯ
                    Button(
                        onClick = {
                            if (!(text.value.length == 16) && (text.value.length > 0)) {
                                val sessionName = text.value.trim()
                                appBarViewModel.addSession(sessionName, selectEvents)
                                appBarViewModel.switchSessions (
                                    sessionsList.value[sessionsList.value.size-1].id
                                )
                                onDismiss()
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.creat)
                        )
                    }

                }

            }
        }
    }
}



