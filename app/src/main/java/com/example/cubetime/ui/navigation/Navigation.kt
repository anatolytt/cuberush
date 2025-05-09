package com.example.cubetime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cubetime.ui.screens.solves.SolvesScreen

import com.example.cubetime.ui.screens.statistics.StatisticsScreen
import com.example.cubetime.ui.screens.timer.TimerScreen
import com.example.cubetime.ui.screens.settings.SettingsDataManager
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.ui.screens.settings.SettingsScreen
import com.example.cubetime.ui.screens.solves.SolvesViewModel
import com.example.cubetime.ui.screens.statistics.StatisticsViewModel
import com.example.cubetime.ui.screens.timer.TimerViewModel


@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: SharedViewModel,
    timerViewModel: TimerViewModel,
    solvesViewModel: SolvesViewModel,
    statisticsViewModel: StatisticsViewModel,
    modifierNavHost: Modifier

    ) {

    NavHost(
        navController = navController,
        modifier = modifierNavHost,
        startDestination = "timer",

    ) {
        composable("timer") {
            TimerScreen(viewModel, timerViewModel)
        }
        composable("solves") {
            SolvesScreen(viewModel, solvesViewModel)
        }
        composable("statistics") {
            StatisticsScreen(viewModel, statisticsViewModel)
        }
        composable("settings") {
            SettingsScreen(viewModel, navController)
        }

    }
}


