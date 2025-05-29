package com.example.cubetime.utils.statistics

import android.util.Log
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.StatType
import com.example.cubetime.data.model.entities.solvesAverages
import kotlinx.coroutines.sync.Mutex

class StatisticsManager () {
    val AVERAGES_LIST = listOf(StatType.MEAN, StatType.MO3, StatType.AO5, StatType.AO12, StatType.AO25, StatType.AO50, StatType.AO100)
    var calculators: MutableMap<Int, StatisticsCalculator> = mutableMapOf()


    private var sessionId: Int = 0
    val mutex = Mutex()


    fun init(initSolves: List<ShortSolve>, sessionId: Int): List<solvesAverages>{
        clear()
        this.sessionId = sessionId
        val result = mutableListOf<solvesAverages>()
        AVERAGES_LIST.forEach { statType ->
            val avgType = statType.getSolvesInAvg()
            calculators[avgType] = if (initSolves.size <= avgType || statType == StatType.MEAN) {
                StatisticsCalculator(initSolves, avgType)
            } else {
                StatisticsCalculator(initSolves.slice(0 .. avgType-1), avgType)
            }

            val calculated = calculators[avgType]!!.initStat()
            if (initSolves.isNotEmpty()) {
//                mutex.withLock {
//                    averages.update {
//                        it + solvesAverages(
//                            solveId = initSolves.first().id,
//                            sessionId = sessionId,
//                            avgType = statType,
//                            result = calculated
//                        )
//                    }
//                }
                result.add(
                    solvesAverages(
                            solveId = initSolves.first().id,
                            sessionId = sessionId,
                            avgType = statType,
                            result = calculated
                        )
                )
            }

        }
        return result
    }


    fun addSolve(solve: ShortSolve) : List<solvesAverages>{
        val result = mutableListOf<solvesAverages>()
        AVERAGES_LIST.forEach { statType ->
            val avgType = statType.getSolvesInAvg()
//            mutex.withLock {
//                averages.update {
//                    it + solvesAverages(
//                        solveId = solve.id,
//                        sessionId = sessionId,
//                        avgType = statType,
//                        result = calculators[avgType]!!.addSolve(solve)
//                    )
//                }
//
//            }
            Log.d("calculators", calculators.toString())
            result.add(
                solvesAverages(
                solveId = solve.id,
                sessionId = sessionId,
                avgType = statType,
                result = calculators[avgType]!!.addSolve(solve)
            )
            )
        }
        return result
    }

        fun recalculateAndGetBests(
        initSolves: List<ShortSolve>,
        start: Int,
        end: Int)
    : List<solvesAverages> {


        val result = mutableListOf<solvesAverages>()

        AVERAGES_LIST.forEach { statType ->
            if (statType == StatType.MEAN) {
                calculators[0] = StatisticsCalculator(initSolves, 0)
                if (initSolves.isNotEmpty()) {
                    result.add(
                        solvesAverages(
                            solveId = initSolves[0].id,
                            sessionId = sessionId,
                            avgType = statType,
                            result = calculators[0]!!.initStat()
                        )
                    )
                }

                initSolves.forEach { solve ->
                    result.add(
                        solvesAverages(
                            solveId = solve.id,
                            sessionId = sessionId,
                            avgType = statType,
                            result = calculators[0]!!.addSolve(solve)
                        )
                    )
                }
            } else {
                val avgType = statType.getSolvesInAvg()
                val before: List<ShortSolve> = initSolves
                    .filter { it.id < start }    // все сборки до изменяемых сборок
                    .sortedBy { it.id }
                val after: List<ShortSolve> = initSolves
                    .filter { it.id > end }    // все сборки после изменяемых сборок
                    .sortedBy { it.id }
                val between: List<ShortSolve> = initSolves
                    .filter { it.id in start..end }
                    .sortedBy { it.id }


                var neededSolves = mutableListOf<ShortSolve>()


                neededSolves += if (before.size > avgType) {
                    before.takeLast(avgType)
                } else {
                    before
                }

                neededSolves += between

                neededSolves += if (after.size > avgType) {
                    after.take(avgType)
                } else {
                    after
                }

                if (neededSolves.size >= avgType && initSolves.isNotEmpty()) {
                    val calculator = StatisticsCalculator(
                        listOf( neededSolves[0]),
                        avgType
                    )

                    result.add(
                        solvesAverages(
                            solveId = neededSolves[0].id,
                            sessionId = sessionId,
                            avgType = statType,
                            result = calculator.initStat()
                        )
                    )

                    neededSolves = neededSolves.drop(1).toMutableList()
                    neededSolves.forEachIndexed { index, solve ->
                        result.add(
                            solvesAverages(
                                solveId = solve.id,
                                sessionId = sessionId,
                                avgType = statType,
                                result = calculator.addSolve(solve)
                            )
                        )
                        if (index == neededSolves.lastIndex && solve.id == initSolves[0].id) {
                            calculators[avgType] = calculator
                        }
                    }
                } else {
                    neededSolves.forEach {solve ->
                        result.add(
                            solvesAverages(
                                solveId = solve.id,
                                sessionId = sessionId,
                                avgType = statType,
                                result = -3
                            )
                        )
                    }

                    calculators[avgType] = StatisticsCalculator(initSolves, avgType)
                    calculators[avgType]!!.initStat()
                }

            }
        }


        return result
}




    private fun clear() {
        calculators = mutableMapOf()
        sessionId = 0
    }
}



