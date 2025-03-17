package com.example.cubetime.ui.navigation

import android.content.Context
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cubetime.ui.screens.solves.SolvesScreen

import com.example.cubetime.ui.screens.statistics.StatisticsScreen
import com.example.cubetime.ui.screens.timer.TimerScreen
import com.example.cubetime.ui.settings.SettingsData
import com.example.cubetime.ui.settings.SettingsDataManager
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.ui.settings.SettingsScreen
import kotlinx.coroutines.flow.Flow



@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: SharedViewModel,
    modifierNavHost: Modifier,

) {

    val context = LocalContext.current
    val settings = SettingsDataManager(context)
    NavHost(
        navController = navController,
        modifier = modifierNavHost,
        startDestination = "timer"
    ) {
        composable(route = "timer") {
            TimerScreen(viewModel)
        }
        composable(route = "solves") {
            SolvesScreen(viewModel)
        }
        composable(route = "statistics") {
            StatisticsScreen(viewModel)
        }
        composable(route = "settings") {
            SettingsScreen(settings)
        }

    }
}


