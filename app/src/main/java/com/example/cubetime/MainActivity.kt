package com.example.cubetime


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.cubetime.model.Events
import com.example.cubetime.ui.appbar.AppBar
import com.example.cubetime.ui.navigation.Navigation
import com.example.cubetime.ui.navigation.bottomNavigationBar.BottomNavigationBar
import com.example.cubetime.ui.navigation.bottomNavigationBar.BottomNavigationItem
import com.example.cubetime.ui.settings.SettingsDataManager
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.ui.theme.CubeTimeTheme
import com.example.cubetime.utils.ChangeLanguage


class MainActivity : ComponentActivity() {
    val viewModel: SharedViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setEvent(Events.CUBE333)
        enableEdgeToEdge()
        setContent {


            val context = LocalContext.current
            val settingsDataManager = SettingsDataManager(context)


            val isDarkMode by settingsDataManager.getTheme().collectAsState(initial = false)
            val language by settingsDataManager.getLanguage().collectAsState(initial = "ru")

            ChangeLanguage(this, language)



            val navController = rememberNavController()

            CubeTimeTheme(
                darkTheme = isDarkMode
            ) {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavigationItem(
                                    name = "Timer",
                                    route = "timer",
                                    icon = Icons.Default.Clear
                                ),
                                BottomNavigationItem(
                                    name = "Solves",
                                    route = "solves",
                                    icon = Icons.Sharp.Home
                                ),
                                BottomNavigationItem(
                                    name = "Statistics",
                                    route = "statistics",
                                    icon = Icons.Sharp.Search
                                ),

                                ),
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route)
                            },
                            modifier = Modifier
                        )
                    },
                    topBar = { AppBar(viewModel, navController) }
                ) { padding ->
                    Navigation(
                        navController,
                        viewModel = viewModel,
                        modifierNavHost = Modifier.padding(padding),
                    )
                }
            }


        }
    }
}


