package com.example.cubetime.utils.statistics

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.cubetime.data.model.ShortSolve

class StatisticsManager (
    val initSolves: List<ShortSolve> = mutableListOf()
) {
    val AVERAGES_LIST = listOf(5, 12, 25, 50, 100)
    var calculators: MutableMap<Int, StatisticsCalculator> = mutableMapOf()
    var averages: MutableMap<Int, Int> = mutableMapOf()
    val bestAverages: MutableMap<Int, Int> = mutableMapOf()

    init {
        Log.d("StatisticsManager", initSolves.toString())
        AVERAGES_LIST.forEach { avgType ->
            calculators[avgType] = StatisticsCalculator(initSolves, avgType)
            averages[avgType] = calculators.get(avgType)!!.initStat()
            bestAverages[avgType] = 0
        }

    }

    fun addSolve(solve: ShortSolve) {
        Log.d("StatisticsManager", initSolves.toString())
        AVERAGES_LIST.forEach { avgType ->
            averages[avgType] = calculators.get(avgType)!!.addSolve(solve)
        }
    }
}