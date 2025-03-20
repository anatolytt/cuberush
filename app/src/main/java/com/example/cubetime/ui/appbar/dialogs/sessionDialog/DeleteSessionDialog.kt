package com.example.cubetime.ui.appbar.dialogs.sessionDialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cubetime.R

@Composable
fun DeleteSessionDialog(
    onDismiss: () -> Unit,
    action: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.delete_session),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(top = 15.dp)
                )

                Text(
                    text = stringResource(R.string.irreversible_action),
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(top = 7.dp, bottom = 15.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    OutlinedButton(onClick = {
                        action()
                        onDismiss()
                    }) {
                        Text(stringResource(R.string.delete))
                    }
                }
            }

        }
    }

}