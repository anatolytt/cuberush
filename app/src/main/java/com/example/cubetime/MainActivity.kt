package com.example.cubetime


import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Scaffold

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesDao
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.ui.appbar.AppBar
import com.example.cubetime.ui.appbar.AppBarViewModel
import com.example.cubetime.ui.navigation.Navigation
import com.example.cubetime.ui.navigation.bottomNavigationBar.BottomNavigationBar
import com.example.cubetime.ui.navigation.bottomNavigationBar.BottomNavigationItem
import com.example.cubetime.ui.screens.settings.Settings
import com.example.cubetime.ui.screens.solves.SolvesViewModel

import com.example.cubetime.ui.screens.settings.SettingsDataManager
import com.example.cubetime.ui.screens.shared_solves.SharedSolvesViewModel
import com.example.cubetime.ui.screens.statistics.StatisticsViewModel
import com.example.cubetime.ui.screens.timer.TimerViewModel
import com.example.cubetime.ui.screens.versus.VersusViewModel
import com.example.cubetime.ui.session_dialogs.SessionDialogsViewModel
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.ui.theme.CubeTimeTheme
import com.example.cubetime.utils.ChangeLanguage


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: SharedViewModel by viewModels()
        val settingsDataManager = SettingsDataManager(this)
        viewModel.setSettingsDataManager(settingsDataManager)

        val timerViewModel : TimerViewModel by viewModels()
        val solvesViewModel : SolvesViewModel by viewModels()
        val statisticsViewModel : StatisticsViewModel by viewModels()
        val appBarViewModel: AppBarViewModel by viewModels()
        val sessionDialogsViewModel: SessionDialogsViewModel by viewModels()
        val sharedSolvesViewModel: SharedSolvesViewModel by viewModels()
        val versusViewModel: VersusViewModel by viewModels()

        AppDatabase.init(this)
        val solvesRepo = SolvesRepository.getInstance(AppDatabase.getInstance().SolvesDao())
        solvesRepo.init()

        enableEdgeToEdge()
        setContent {
            val language by settingsDataManager.getLanguage().collectAsState(initial = "ru")
            val theme by settingsDataManager.getTheme().collectAsState(initial = false)

            ChangeLanguage(this, language)
            val navController = rememberNavController()


            CubeTimeTheme(
                isCustomDarkTheme = theme
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(
                            visible = (viewModel.showTopBottom),
                            exit = fadeOut() + slideOutVertically { it }) {
                            BottomNavigationBar(
                                items = listOf(
                                    BottomNavigationItem(
                                        name = "Timer",
                                        route = "timer",
                                        icon = painterResource(R.drawable.clockalarm_1_)
                                    ),
                                    BottomNavigationItem(
                                        name = "Solves",
                                        route = "solves",
                                        icon = painterResource(R.drawable.appssolid_1_)
                                    ),
                                    BottomNavigationItem(
                                        name = "Statistics",
                                        route = "statistics",
                                        icon = painterResource(R.drawable.stats)
                                    ),

                                    ),
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route)
                                },
                                modifier = Modifier,
                                hideEverything = viewModel.everythingHidden
                            )
                        }
                    },
                    topBar = {
                        AnimatedVisibility(
                            visible = viewModel.showTopBottom,
                            exit = fadeOut() + slideOutVertically { it }
                        ) {
                            AppBar(
                                viewModel = viewModel,
                                appBarViewModel = appBarViewModel,
                                sessionDialogsViewModel = sessionDialogsViewModel,
                                navController = navController
                            )
                        }
                    }
                ) { padding ->
                    Navigation(
                        navController,
                        viewModel = viewModel,
                        modifierNavHost = Modifier.padding(padding),
                        timerViewModel = timerViewModel,
                        solvesViewModel = solvesViewModel,
                        statisticsViewModel = statisticsViewModel,
                        sharedSolvesViewModel = sharedSolvesViewModel,
                        sessionDialogsViewModel = sessionDialogsViewModel,
                        versusViewModel = versusViewModel
                    )
                }
            }


        }
    }
}


