package com.example.cubetime


import ScramblerScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.sharp.AddCircle
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation
import androidx.navigation.compose.rememberNavController
import com.example.cubetime.model.Events
import com.example.cubetime.ui.appbar.AppBar
import com.example.cubetime.ui.navigation.Navigation
import com.example.cubetime.ui.navigation.bottomNavigationBar.BottomNavigationBar
import com.example.cubetime.ui.navigation.bottomNavigationBar.BottomNavigationItem
import com.example.cubetime.ui.theme.CubeTimeTheme
import com.example.cubetime.utils.Scrambler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            CubeTimeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar (
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
                    topBar = { AppBar(this@MainActivity) }
                ) { padding ->
                        Navigation (
                            navController,
                            modifierNavHost = Modifier.padding(padding)
                        )
                }
            }

            ScramblerScreen(scrambler = Scrambler(), event = Events.MEGA)
        }
    }
}


