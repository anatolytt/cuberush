package com.example.cubetime.ui.appbar.dialogs.sessionDialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cubetime.R

@Composable
fun EditSessionDialog(
    onDismiss: () -> Unit,
    action: (String) -> Unit)
    {
        var enteredText = remember { mutableStateOf("") }


        Dialog(
            onDismissRequest = onDismiss
        ) {
            Card (
                modifier = Modifier
                    .fillMaxWidth()

            ) {

                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.edit_session_name),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(top = 15.dp)
                    )
                    SessionTextField(enteredText)


                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Text(stringResource(R.string.cancel))
                        }

                        OutlinedButton(onClick = {
                            if (!(enteredText.value.length == 16) && (enteredText.value.length > 0)) {
                                action(enteredText.value)
                                onDismiss()

                            }
                        }) {
                            Text(stringResource(R.string.done))
                        }
                    }
                }

            }
        }

    }