package com.example.cubetime.ui.screens.timer.dialogs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cubetime.R
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.ui.shared.PenaltiesDropdownMenu
import com.example.cubetime.utils.TimeFormat

@Composable
fun AddTimeDialog(
    onDismiss: () -> Unit,
    action: (String, Penalties) -> Unit
) {
    var enteredText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedPenalty by remember { mutableStateOf(Penalties.NONE) }
    val focusRequester = remember { FocusRequester() }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()

        ) {

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.enter_time),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 30.dp)
                    ) {
                        OutlinedTextField(
                            value = enteredText,
                            visualTransformation = { it ->
                                TimeFormat.inputTextVisualTransformation(
                                    it
                                )
                            },
                            onValueChange = { newText ->
                                if (newText.text.length <= 6) {
                                    enteredText =
                                        newText.copy(selection = TextRange(enteredText.text.length + 1))
                                }

                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            textStyle = TextStyle(fontSize = 25.sp),
                            modifier = Modifier
                                .width(150.dp)
                                .focusRequester(focusRequester)


                        )
                    }

                    PenaltiesDropdownMenu(
                        setPenalty = {penalty -> selectedPenalty = penalty},
                        iconSize = 50.dp
                    )

                }

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
                        action(enteredText.text, selectedPenalty)
                        onDismiss()
                    }) {
                        Text(stringResource(R.string.done))
                    }
                }
            }

        }
    }

}

