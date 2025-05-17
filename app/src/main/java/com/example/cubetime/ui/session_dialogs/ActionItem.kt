package com.example.cubetime.ui.session_dialogs

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ActionItem (
    onClick: () -> Unit,
    backgroundColor: Color,
    icon: ImageVector,
    iconTint: Color = Color.White
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .background(backgroundColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            // tint = iconTint
        )
    }
}