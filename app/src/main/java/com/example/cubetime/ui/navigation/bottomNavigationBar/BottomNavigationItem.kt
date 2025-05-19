package com.example.cubetime.ui.navigation.bottomNavigationBar

import androidx.compose.ui.graphics.painter.Painter

data class BottomNavigationItem(
    val name: String,
    val route: String,
    val icon: Painter
)
