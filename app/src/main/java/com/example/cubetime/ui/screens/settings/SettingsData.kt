package com.example.cubetime.ui.screens.settings


data class Settings(
    val timerInspection:Boolean = false,
    val timerHideTime: Boolean = false,
    val timerDelay: Boolean = false,
    val printScrambles: Boolean = true
)