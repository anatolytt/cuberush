package com.example.cubetime.ui.appbar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cubetime.R
import com.example.cubetime.ui.session_dialogs.SessionDialogsNavigation
import com.example.cubetime.ui.session_dialogs.DialogsState
import com.example.cubetime.ui.session_dialogs.SessionDialogsViewModel
import com.example.cubetime.ui.shared.SharedViewModel

enum class DialogsState { EVENT, SESSION, NONE }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    viewModel: SharedViewModel,
    appBarViewModel: AppBarViewModel,
    sessionDialogsViewModel: SessionDialogsViewModel,
    navController: NavHostController,
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val dialogToShow =  remember { mutableStateOf(DialogsState.NONE) }
    val currentSession by appBarViewModel.currentSession.collectAsState()


    val offsetY by animateFloatAsState(
        targetValue = if (viewModel.everythingHidden) -600f else 0f,
        animationSpec = tween(durationMillis = 300)
    )


    if (!viewModel.deleteSolveAppBar || currentRoute != "solves") {
        SessionDialogsNavigation(
            dialogToShow,
            sessionDialogsViewModel = sessionDialogsViewModel,
            sessionOnCLick = { session -> sessionDialogsViewModel.switchSessions(session.id) }
        )

        CenterAlignedTopAppBar(
            title = {
                Row (verticalAlignment = Alignment.CenterVertically) {
                    Icon (
                        painter = painterResource(currentSession.event.getIconDrawableId()),
                        contentDescription = stringResource(currentSession.event.getEventStringId()),
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

                })
                {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Open settings"
                    )
                }
            },
            actions = {
                //кнопка для сражения
                IconButton(onClick = {
                    navController.navigate("versus")
                }) {
                    Icon(
                        painter = painterResource(R.drawable.versus_icon),
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Change session dialog"
                    )
                }
                IconButton(onClick = { dialogToShow.value = DialogsState.SESSION }) {
                    Icon(
                        painter = painterResource(R.drawable._square_svgrepo_com_1_),
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
    } else {
        TopAppBar(
            title = { Text("Выбрано элементов") },
            navigationIcon = {
                IconButton(onClick = {
                    viewModel.changeAppBar()

                }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "выйти из режима выбора"
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    viewModel.deleteSolves()
                    viewModel.changeAppBar()
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "удалить выбранное"
                    )
                }
            }
        )
    }

}
