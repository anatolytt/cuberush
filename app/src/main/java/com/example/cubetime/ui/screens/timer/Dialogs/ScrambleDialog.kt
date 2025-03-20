package com.example.cubetime.ui.screens.timer.dialogs

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cubetime.R

@Composable
fun ScrambleDialog(
    onDismiss: () -> Unit,
    action: (String) -> Unit
) {
    var enteredText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth()

        ) {

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.enter_scramble),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(top = 15.dp)
                )
                TextField(
                    value = enteredText,
                    onValueChange = { enteredText = it },
                    textStyle = TextStyle(fontSize = 15.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp)
                        .focusRequester(focusRequester)

                )

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
                        action(enteredText)
                        onDismiss()
                    }) {
                        Text(stringResource(R.string.done))
                    }
                }
            }

        }
    }

}