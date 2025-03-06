package com.example.cubetime.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cubetime.ui.screens.solves.Solves
import com.example.cubetime.ui.screens.statistics.Statistics
import com.example.cubetime.ui.screens.timer.Timer

@Composable
fun Navigation(navController: NavHostController, modifierNavHost: Modifier) {
    NavHost(
        navController = navController,
        modifier = modifierNavHost,
        startDestination = "timer") {
        composable(route = "timer") {
            Timer()
        }
        composable(route = "solves") {
            Solves()
        }
        composable(route = "statistics") {
            Statistics()
        }
    }
}
