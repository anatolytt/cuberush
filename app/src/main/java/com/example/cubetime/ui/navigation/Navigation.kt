package com.example.cubetime.ui.navigation

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.cubetime.data.remote.HttpRoutes
import com.example.cubetime.data.remote.SolvesAPI
import com.example.cubetime.ui.appbar.AppBarViewModel
import com.example.cubetime.ui.screens.solves.SolvesScreen

import com.example.cubetime.ui.screens.statistics.StatisticsScreen
import com.example.cubetime.ui.screens.timer.TimerScreen
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.ui.screens.settings.SettingsScreen
import com.example.cubetime.ui.screens.shared_solves.SharedSolvesScreen
import com.example.cubetime.ui.screens.shared_solves.SharedSolvesViewModel
import com.example.cubetime.ui.screens.solves.SolvesViewModel
import com.example.cubetime.ui.screens.statistics.StatisticsViewModel
import com.example.cubetime.ui.screens.timer.TimerViewModel
import com.example.cubetime.ui.screens.versus.VersusScreen
import com.example.cubetime.ui.screens.versus.VersusViewModel
import com.example.cubetime.ui.session_dialogs.SessionDialogsViewModel


@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: SharedViewModel,
    timerViewModel: TimerViewModel,
    solvesViewModel: SolvesViewModel,
    statisticsViewModel: StatisticsViewModel,
    sharedSolvesViewModel: SharedSolvesViewModel,
    sessionDialogsViewModel: SessionDialogsViewModel,
    versusViewModel: VersusViewModel,
    modifierNavHost: Modifier

    ) {

    NavHost(
        navController = navController,
        modifier = modifierNavHost,
        startDestination = "timer",

    ) {
        composable("timer") {
            viewModel.changeTopBottomVisibility(true)
            TimerScreen(viewModel, timerViewModel)
        }
        composable("solves") {
            viewModel.changeTopBottomVisibility(true)
            SolvesScreen(viewModel, solvesViewModel)
        }
        composable("statistics") {
            viewModel.changeTopBottomVisibility(true)
            StatisticsScreen(viewModel, statisticsViewModel)
        }
        composable("settings") {
            viewModel.changeTopBottomVisibility(false)
            SettingsScreen(viewModel, navController)
        }
        composable("versus"){
            viewModel.changeTopBottomVisibility(false)
            VersusScreen(
                viewModel,
                navController,
                versusViewModel,
                sessionDialogsViewModel
            )
        }

        composable(
            route = "sharedSolves" ,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://cuberush.up.railway.app/solves/{token}"
                    action = Intent.ACTION_VIEW
                }
            ),
            arguments = listOf(
                navArgument("token") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { entry ->
            viewModel.changeTopBottomVisibility(false)
            val token = entry.arguments?.getString("token") ?: ""
            Log.d("TOKEN", token)
            SharedSolvesScreen(
                sharedViewModel = viewModel,
                sharedSolvesViewModel = sharedSolvesViewModel,
                sessionDialogsViewModel = sessionDialogsViewModel,
                navController = navController,
                solvesToken = token
            )
        }

    }
}


