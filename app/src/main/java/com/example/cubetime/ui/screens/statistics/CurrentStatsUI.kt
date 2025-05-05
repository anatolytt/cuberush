package com.example.cubetime.ui.screens.statistics

import com.example.cubetime.data.model.StatType

data class CurrentStatsUI(
    val single: String = "-3",
    val mean: String = "-3",
    val mo3: String = "-3",
    val ao5: String = "-3",
    val ao12: String = "-3",
    val ao25: String = "-3",
    val ao50: String = "-3",
    val ao100: String = "-3"
) {
    fun getStat(statType: StatType): String {
        return when (statType) {
            StatType.SINGLE -> single
            StatType.MEAN -> mean
            StatType.MO3 -> mo3
            StatType.AO5 -> ao5
            StatType.AO12 -> ao12
            StatType.AO25 -> ao25
            StatType.AO50 -> ao50
            StatType.AO100 -> ao100
        }
    }
}
