package com.example.cubetime.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cubetime.ui.screens.solves.SolvesScreen
import com.example.cubetime.ui.screens.statistics.StatisticsScreen
import com.example.cubetime.ui.screens.timer.TimerScreen
import com.example.cubetime.ui.shared.SharedViewModel

@Composable
fun Navigation(navController: NavHostController, viewModel: SharedViewModel, modifierNavHost: Modifier) {
    NavHost(
        navController = navController,
        modifier = modifierNavHost,
        startDestination = "timer") {
        composable(route = "timer") {
            TimerScreen(viewModel)
        }
        composable(route = "solves") {
            SolvesScreen(viewModel)
        }
        composable(route = "statistics") {
            StatisticsScreen(viewModel)
        }
    }
}
