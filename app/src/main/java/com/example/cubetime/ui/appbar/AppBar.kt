package com.example.cubetime.ui.appbar

import android.content.Context
import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.cubetime.ui.appbar.dialogs.EventDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(context: Context, event: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = event,
            )
                },
        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            IconButton(onClick = { Toast.makeText(context, "Navigate to settings", Toast.LENGTH_SHORT).show() }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Open settings"
                )
            }
        },
        actions = {
            IconButton(onClick = { Toast.makeText(context, "Change mode dialog", Toast.LENGTH_SHORT).show() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "Change session dialog"
                )
            }
            IconButton(onClick = { Toast.makeText(context, "Change session dialog", Toast.LENGTH_SHORT).show() }) {
                Icon(
                    imageVector = Icons.Default.Face,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "Sessions dialog"
                )
            }
        },
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))

    )
}
