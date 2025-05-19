package com.example.cubetime.utils

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import com.example.cubetime.data.model.AverageResult
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.data.model.StatType
import com.example.cubetime.shared.AppStrings
import com.example.cubetime.ui.screens.statistics.CurrentStatsUI
import java.util.Locale
import kotlin.math.ceil

object TimeFormat {
    fun millisToString(
        millis: Int,
        penalty: Penalties = Penalties.NONE,
        showDnfTime: Boolean = false
    ): String {
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
                (millisAfterPenalty.toDouble()) / 1000
            ) + plus
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

    fun solveToResult(solve: ShortSolve): Int {
        return when (solve.penalties) {
            Penalties.NONE -> solve.result
            Penalties.PLUS2 -> solve.result + 2000
            Penalties.DNF -> Int.MAX_VALUE
        }
    }

    fun solveToMillis(solve: Solve): Int {
        return when (solve.penalties) {
            Penalties.PLUS2 -> solve.result + 2000
            Penalties.DNF -> Int.MAX_VALUE
            Penalties.NONE -> solve.result
        }
    }

    fun inputTextToMillis(input: String): Int {
        val inputText = input.padStart(6, '0')
        val minutes = inputText.slice(0..1).toInt()
        val seconds = inputText.slice(2..3).toInt()
        val milliseconds = inputText.slice(4..5).toInt()
        return milliseconds * 10 + seconds * 1000 + minutes * 60000
    }


    fun statToString(statResult: Int): String {
        return when (statResult) {
            -3 -> AppStrings.emptyResult
            -1 -> "DNF"
            else -> millisToString(statResult)
        }
    }


    fun mapToUIStats(averages: Map<StatType, AverageResult>): CurrentStatsUI {
        return CurrentStatsUI(
            mean = statToString(averages[StatType.MEAN]?.result ?: -3),
            single = statToString(averages[StatType.SINGLE]?.result ?: -3),
            mo3 = statToString(averages[StatType.MO3]?.result ?: -3),
            ao5 = statToString(averages[StatType.AO5]?.result ?: -3),
            ao12 = statToString(averages[StatType.AO12]?.result ?: -3),
            ao25 = statToString(averages[StatType.AO25]?.result ?: -3),
            ao50 = statToString(averages[StatType.AO50]?.result ?: -3),
            ao100 = statToString(averages[StatType.AO100]?.result ?: -3),
        )

    }


    fun solveListToStringAverageList(
        solves: List<Solve>,
        statType: StatType = StatType.MEAN
    ): List<Pair<Solve, String>> {
        if (statType == StatType.MEAN || statType == StatType.MO3) {
            return solves.map { Pair(it, millisToString(it.result, it.penalties)) }
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
        val text =
            if (input.text.length > 6) input.text.takeLast(6) else input.text.padStart(6, '0')
        val result = "${text.substring(0..1)}:${text.substring(2..3)}.${text.substring(4..5)}"

        val indicesOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return 8
            }

            override fun transformedToOriginal(offset: Int): Int {
                return 0
            }
        }
        return TransformedText(AnnotatedString(result), indicesOffsetTranslator)
    }


    //промежуток
    //рассмотреть случай когда 22.
    fun inputTextSearchToMaxMin(searchTextField: String): Pair<Int, Int> {
        try {
            if (searchTextField.isEmpty()) {
                Log.d("MyTag1", "тут я")
                return Pair(0, Int.MAX_VALUE)
            }
            //точка и двоеточие + длина больше требуемогоо
            else if ((searchTextField.contains(":") &&
                        searchTextField.contains(".") && searchTextField.length > 8)
            ) {

                return Pair(0, 0)
            }
            //случай 22:22.22

            //22:
            else if (searchTextField.endsWith(":") && searchTextField.length == 3) {
                val colon = searchTextField.dropLast(1).toInt()

                val min = colon * 60 * 1000
                val max = (colon + 1) * 60 * 1000 - 1


                return Pair(min, max)
            }
            //22:1
            else if (searchTextField.length == 4 && searchTextField[2] == ':') {
                val colon = searchTextField.split(":")

                val first = colon[0].toInt() // 22 - минуты
                val second = colon[1].toInt()//1 - часть секунды

                val min = first * 60 * 1000 + (second) * 10000
                val max = first * 60 * 1000 + (second + 1) * 10000 - 1

                return Pair(min, max)
            }
            //22:10(good)
            else if (searchTextField.length == 5 && searchTextField[2] == ':') {
                val colon = searchTextField.split(":")

                val first = colon[0].toInt() // 22 - минуты
                val second = colon[1].toInt()//10 - часть секунды

                val min = first * 60 * 1000 + (second) * 1000
                val max = first * 60 * 1000 + (second + 1) * 1000 - 1



                return Pair(min, max)
            }
            //22:10.(good)
            else if (searchTextField.length == 6 && searchTextField.contains(":")
                && searchTextField.endsWith(".")

            ) {
                val colon = searchTextField.dropLast(1).split(":")

                val first = colon[0].toInt() // 22 - минуты
                val second = colon[1].toInt()//10 - часть секунды

                val min = first * 60 * 1000 + (second) * 1000
                val max = first * 60 * 1000 + (second + 1) * 1000 - 1

                return Pair(min, max)

            }
            //22:10.5(good)
            else if (searchTextField.length == 7 && searchTextField[2] == ':' &&
                searchTextField[5] == '.'
            ) {
                val colon = searchTextField.split(":")

                //22 + 10.5
                val minutes = colon[0].toInt()//минуты
                //10 + 5
                val second = colon[1].split(".")

                val sec = second[0].toInt() // секунды
                val ml = second[1].toInt()//милли

                val min = minutes * 60 * 1000 + sec * 1000 + ml * 100
                val max = minutes * 60 * 1000 + sec * 1000 + (ml + 1) * 100 - 1


                return Pair(min, max)

            }
            //22:10.53(good)
            else if (searchTextField.contains(":") &&
                searchTextField.contains(".") && searchTextField.length == 8
            ) {
                val colon = searchTextField.split(":")

                //22 + 10.5
                val minutes = colon[0].toInt()//минуты
                //10 + 5
                val second = colon[1].split(".")

                val sec = second[0].toInt() // секунды
                val ml = second[1].toInt()//милли

                val min = minutes * 60 * 1000 + sec * 1000 + ml * 10
                val max = minutes * 60 * 1000 + sec * 1000 + (ml + 1) * 10 - 1



                return Pair(min, max)
            }

            //минутное время (когда ввелось  число в формате 1:)
            //1:
            else if (searchTextField.endsWith(":")) {

                val colon = searchTextField.dropLast(1).toInt()

                return Pair(colon * 60 * 1000, (colon + 1) * 60 * 1000 - 1)

            }
            //1:0
            else if (searchTextField.length == 3 && searchTextField[1] == ':') {
                //1:0
                val colon = searchTextField.split(":")

                //1 и 0
                val first = colon[0].toInt()
                val second = colon[1].toInt()

                val min = first * 60 * 1000 + (second) * 10000
                val max = first * 60 * 1000 + (second + 1) * 10000 - 1

                return Pair(min, max)

            }
            //1:05
            else if (searchTextField.contains(":") && searchTextField.length == 4) {
                //1:05
                val colon = searchTextField.split(":")

                // 1 и 05
                val first = colon[0].toInt()
                val second = colon[1].toInt()


                val min = first * 60 * 1000 + second * 1000
                val max = first * 60 * 1000 + (second + 1) * 1000 - 1



                return Pair(min, max)

            }
            //1:05.
            else if (searchTextField.length == 5 && searchTextField.endsWith('.') && searchTextField[1] == ':'

            ) {
                val colon = searchTextField.dropLast(1).split(":")
                // 1 и 05
                val first = colon[0].toInt()
                val second = colon[1].toInt()

                val min = first * 60 * 1000 + second * 1000
                val max = first * 60 * 1000 + (second + 1) * 1000 - 1

                return Pair(min, max)
            }
            //1:05.2
            else if (searchTextField.contains(":") && searchTextField.contains(".")
                && searchTextField.length == 6
            ) {
                // 1 + 05.2
                val colon = searchTextField.split(":")
                // 1 + 05.2
                val minutes = colon[0].toInt() // минуты
                val secMls = colon[1] // секунды + млс

                //делим (сек + млс ) на части
                // 05 + 2
                val splitSecMls = secMls.split(".")

                val sec = splitSecMls[0].toInt() // секунды
                val ml = splitSecMls[1].toInt() // миллисекунды

                val min = minutes * 60 * 1000 + sec * 1000 + ml * 100
                val max = minutes * 60 * 1000 + sec * 1000 + (ml + 1) * 100 - 1

                return Pair(min, max)
            }
            //1:05.24
            else if (searchTextField.contains(":") && searchTextField[4] == '.'
                && searchTextField.length == 7
            ) {
                // 1 + 05.24
                val colon = searchTextField.split(":")
                // 1 + 05.24
                val minutes = colon[0].toInt() // минуты
                val secMls = colon[1] // секунды + млс

                //делим (сек + млс ) на части
                // 05 + 24
                val splitSecMls = secMls.split(".")

                val sec = splitSecMls[0].toInt() // секунды
                val ml = splitSecMls[1].toInt() // миллисекунды

                val min = minutes * 60 * 1000 + sec * 1000 + ml * 10
                val max = minutes * 60 * 1000 + sec * 1000 + (ml + 1) * 10

                return Pair(min, max)
            }
            //2.
            else if (searchTextField.length == 2 && searchTextField.endsWith(".")) {
                val dot = searchTextField.dropLast(1).toInt()

                val min = dot * 1000
                val max = (dot + 1) * 1000 - 1

                return Pair(min, max)
            }
            //22.
            else if (searchTextField.length == 3 && searchTextField.endsWith(".")) {
                val dot = searchTextField.dropLast(1).toInt()

                val min = dot * 1000
                val max = (dot + 1) * 1000 - 1

                return Pair(min, max)
            }
            //22.2
            else if (searchTextField.length == 4 && searchTextField.contains(".")) {
                val dot = searchTextField.split(".")

                val first = dot[0].toInt() // секунды
                val second = dot[1].toInt()//млс

                val min = first * 1000 + second * 100
                val max = first * 1000 + (second + 1) * 100 - 1

                return Pair(min, max)
            } else if (searchTextField.contains(".") && searchTextField.length == 3) {
                val dot = searchTextField.split(".")

                val first = dot[0].toInt()
                val second = dot[1].toInt()

                val min = first * 1000 + second * 100
                val max = first * 1000 + (second + 1) * 100 - 1

                return Pair(min, max)

            } else if (searchTextField.contains(".")) {
                val timeInMs = (searchTextField.toDouble() * 1000).toInt()

                return Pair(timeInMs - 1, timeInMs + 1)
            } else {
                val seconds = searchTextField.toInt()

                return Pair(seconds * 1000, (seconds + 1) * 1000 - 1)
            }
        } catch (e: Exception) {
            // исключения

            return Pair(0, 0)
        }

    }

    fun solveToResult(solve: ShortSolve): Int {
        return when (solve.penalties) {
            Penalties.NONE -> solve.result
            Penalties.PLUS2 -> solve.result + 2000
            Penalties.DNF -> Int.MAX_VALUE
        }
    }
}



