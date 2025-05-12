package com.example.cubetime.ui.screens.versus.Dialogs

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalTopSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState,
)
{
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        modifier = Modifier.graphicsLayer { rotationX = 180f }
    ) {

        Text("UwlkmU")
    }
}