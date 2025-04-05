package com.example.cubetime.ui.appbar

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cubetime.data.model.Events
import com.example.cubetime.ui.appbar.dialogs.AppBarDialogNavigation
import com.example.cubetime.ui.appbar.dialogs.DialogsState
import com.example.cubetime.ui.appbar.dialogs.EventDialog
import com.example.cubetime.ui.shared.SharedViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(viewModel: SharedViewModel,
           navController: NavHostController,
) {
    val appBarViewModel : AppBarViewModel = viewModel()
    val dialogToShow =  remember {
        mutableStateOf<DialogsState>(DialogsState.NONE)
    }

    val offsetY by animateFloatAsState(
        targetValue = if (viewModel.everythingHidden) -600f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    val currentSession by appBarViewModel.repository.currentSession.collectAsState()

    AppBarDialogNavigation(dialogToShow, viewModel = viewModel, appBarViewModel=appBarViewModel)

    CenterAlignedTopAppBar(
        title = {
            val event = currentSession.event
            Row (verticalAlignment = Alignment.CenterVertically) {
                Icon (
                    painter = painterResource(event.getIconDrawableId()),
                    contentDescription = stringResource(event.getEventStringId()),
                    Modifier.size(20.dp)
                )
                Text(
                    text = currentSession.name,
                    Modifier.padding(start = 10.dp)
                )
            }
                },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate("settings")
                viewModel.changeSettingsVisibility()
            })
            {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Open settings"
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "Change session dialog"
                )
            }
            IconButton(onClick = { dialogToShow.value = DialogsState.SESSION }) {
                Icon(
                    imageVector = Icons.Default.Face,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "Sessions dialog"
                )
            }
        },

        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))

            .graphicsLayer(
                translationY = offsetY
            )


    )
}
