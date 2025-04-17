package com.example.cubetime.ui.appbar.dialogs.sessionDialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cubetime.R
import com.example.cubetime.data.model.Session

@Composable
fun SessionTextField(text: MutableState<String>){


    var isErrorTextLength = (text.value.length == 16)
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        value = text.value,
        isError = (isErrorTextLength),
        onValueChange = { newText ->
            if (newText.length <= 16) {
                text.value = newText
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp)
            .focusRequester(focusRequester),

        label = {
            if (isErrorTextLength)
                Text(text= stringResource(R.string.maxlength),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
        },
        placeholder = { Text(text = stringResource(R.string.edittext)) }

    )
}

