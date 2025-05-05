package com.example.cubetime.utils

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import com.example.cubetime.data.model.AverageResult
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Solve
import com.example.cubetime.data.model.Stat
import com.example.cubetime.data.model.StatType
import com.example.cubetime.shared.AppStrings
import com.example.cubetime.ui.screens.statistics.CurrentStatsUI
import java.util.Locale
import kotlin.math.ceil

object TimeFormat {
    fun millisToString(
        millis: Int,
        penalty: Penalties = Penalties.NONE,
        showDnfTime: Boolean = false): String {
        var plus = ""
        var millisAfterPenalty = millis
        if (penalty == Penalties.DNF) {
            return "DNF" + if (showDnfTime) "(${millisToString(millis)})" else ""
        }
        if (penalty == Penalties.PLUS2) {
            plus = "+"
            millisAfterPenalty += 2000
        }

        if (millisAfterPenalty < 60000) {
            return (String.format(
                Locale.US,
                "%.2f",
                (millisAfterPenalty.toDouble()) / 1000)+plus
                    )
        } else {
            val minutes = millisAfterPenalty / 60000
            val millisLeft = millisAfterPenalty % 60000
            val milliseconds = millisLeft % 1000 / 10
            var seconds = (millisLeft / 1000).toString()
            if (seconds.length == 1) {
                seconds = "0" + seconds
            }
            return ("${minutes}:${seconds}.${milliseconds}${plus}")
        }
    }

    fun solveToMillis(solve: Solve) : Int{
        return when (solve.penalties) {
            Penalties.PLUS2 -> solve.result + 2000
            Penalties.DNF -> Int.MAX_VALUE
            Penalties.NONE -> solve.result
        }
    }

    fun inputTextToMillis(input: String): Int {
        val inputText = input.padStart(6, '0')
        val minutes = inputText.slice(0 .. 1).toInt()
        val seconds = inputText.slice(2 .. 3).toInt()
        val milliseconds = inputText.slice(4 .. 5).toInt()
        return milliseconds*10 + seconds*1000 + minutes*60000
    }



    fun statToString(statResult: Int) : String {
        return when (statResult) {
            -3 -> AppStrings.emptyResult
            -1 -> "DNF"
            else -> millisToString(statResult)
        }
    }



    fun mapToUIStats(averages: Map<StatType, AverageResult>) : CurrentStatsUI {
        return CurrentStatsUI(
            mean = statToString(averages[StatType.MEAN]?.result ?: -3),
            single = statToString(averages[StatType.SINGLE]?.result ?: -3),
            mo3 = statToString(averages[StatType.MO3]?.result ?: -3) ,
            ao5 = statToString(averages[StatType.AO5]?.result ?: -3),
            ao12 = statToString(averages[StatType.AO12]?.result ?: -3),
            ao25 = statToString(averages[StatType.AO25]?.result ?: -3),
            ao50 = statToString(averages[StatType.AO50]?.result ?: -3),
            ao100 = statToString(averages[StatType.AO100]?.result ?: -3),
        )

    }

    fun getBestEndAndWorstStartIdxs(solves: List<Int>): Pair<Int, Int> {
        val solvesInAvg = solves.size
        val sorted = solves.sorted()
        val notCountingSolves = if (solvesInAvg == 3) 0 else ceil(solvesInAvg * 0.05).toInt()
        val lastStartIdx = notCountingSolves - 1
        val firstEndIdx = solvesInAvg - lastStartIdx - 1
        return Pair(lastStartIdx, firstEndIdx)
    }

    fun solveListToStringAverageList(solves: List<Solve>, statType: StatType): List<Pair<Solve, String>> {
        if (statType == StatType.MEAN || statType == StatType.MO3) {
            return solves.map {Pair(it, millisToString(it.result, it.penalties))}
        }
        val sorted: List<Int> = solves.map { solveToMillis(it) }.sorted()
        val notCountingSolves = ceil(statType.getSolvesInAvg() * 0.05).toInt()
        val lastBestSolve = sorted[notCountingSolves - 1]
        val firstEndSolve = sorted[statType.getSolvesInAvg() - notCountingSolves]

        var dnfsCounter = 0
        return solves.map { solve ->
            val solveStr = millisToString(solve.result, solve.penalties, showDnfTime = true)
            val solveToShow = if (solveToMillis(solve) <= lastBestSolve) {
                "($solveStr)"
             } else if (solveToMillis(solve) >= firstEndSolve && dnfsCounter < notCountingSolves) {
                if (solve.penalties == Penalties.DNF) dnfsCounter += 1
                "($solveStr)"
             } else {
                solveStr
             }
            Pair(solve, solveToShow)
        }.reversed()
    }


    fun inputTextVisualTransformation(input: AnnotatedString): TransformedText {
        val text = if (input.text.length > 6) input.text.takeLast(6) else input.text.padStart(6, '0')
        val result = "${text.substring(0 .. 1)}:${text.substring(2..3)}.${text.substring(4..5)}"

        val indicesOffsetTranslator = object: OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return 8
            }
            override fun transformedToOriginal(offset: Int): Int {
                return 0
            }
        }
        return TransformedText(AnnotatedString(result), indicesOffsetTranslator)
    }




}