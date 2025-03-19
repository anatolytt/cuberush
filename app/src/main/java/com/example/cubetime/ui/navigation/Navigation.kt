package com.example.cubetime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cubetime.ui.screens.solves.SolvesScreen

import com.example.cubetime.ui.screens.statistics.StatisticsScreen
import com.example.cubetime.ui.screens.timer.TimerScreen
import com.example.cubetime.ui.settings.SettingsDataManager
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.ui.settings.SettingsScreen


@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: SharedViewModel,
    modifierNavHost: Modifier,
    settingsDataManager: SettingsDataManager
) {

    NavHost(
        navController = navController,
        modifier = modifierNavHost,
        startDestination = "timer",

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
            SettingsScreen(settingsDataManager, viewModel,navController)
        }

    }
}


