package com.example.cubetime.ui.navigation.bottomNavigationBar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cubetime.R
import com.example.cubetime.ui.shared.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    items : List<BottomNavigationItem>,
    navController: NavController,
    modifier: Modifier,
    onItemClick: (BottomNavigationItem) -> Unit,
    hideEverything: Boolean
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val offsetY by animateFloatAsState(
        targetValue = if (hideEverything) 600f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    NavigationBar(
        modifier = modifier.graphicsLayer(translationY = offsetY),
        tonalElevation = 20.dp
    ) {
        val route = backStackEntry.value?.destination?.route
        items.forEach { item ->
            val selected = item.route == route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = item.name + " icon"
                    )
                },
                label = { Text(item.name) }
            )
        }
    }
}
