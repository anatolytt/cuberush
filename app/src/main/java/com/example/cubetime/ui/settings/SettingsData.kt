package com.example.cubetime.ui.settings

data class SettingsData(
    val isDarkMode: Boolean,
    val language: String,
    val isInspectionEnabled:Boolean = false,
    val timehidden: Boolean = false,
    val delay: Boolean = false

)