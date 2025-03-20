package com.example.cubetime.ui.appbar.dialogs

import android.widget.Spinner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cubetime.R
import com.example.cubetime.model.Events
import com.example.cubetime.model.Session
import com.example.cubetime.ui.shared.SharedViewModel


@Composable
fun SessionDialog(
    onDismiss : () -> Unit,
    onNext: () -> Unit,
    viewModel: SharedViewModel) {


    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(20.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column (
                modifier = Modifier.fillMaxSize()
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
                        fontWeight = FontWeight.Bold,
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
                        .height(1.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(viewModel.sessions.size) { index ->
                        val session = viewModel.sessions[index]
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 22.dp)
                                .clickable { viewModel.switchSessions(index) },

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

