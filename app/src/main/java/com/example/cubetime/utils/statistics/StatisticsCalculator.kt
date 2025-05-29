package com.example.cubetime.utils.statistics

import android.util.Log
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.utils.TimeFormat
import java.io.Serializable
import kotlin.math.ceil


/*
    return -3 - среднее не посчитано
    return -1 - DNF
 */


class StatisticsCalculator (
    var solvesInput: List<ShortSolve>,
    var solvesInAvg: Int, // Всего сборок в среднем,
) {
    /*
        Храним и список со всеми сборками, и список с отсорированными сборкамиб чтобы не сортировать
        список с нуля при каждом добавлении сборки.
     */
    private var solves = mutableListOf<Int>()
    private var sortedSolves = mutableListOf<Int>()


    private var lastSum: Int = 0    // Сумма сборок в зачёт последнего подсчитанного среднего

    // Количествос сборок, не идущих в зачет с каждой стороны
    private var notCountingSolves: Int = 0

    // Количество сборок, идущих в зачет
    private var countingSolves: Int = 0

    // Количество днфов в зачет в текущем списке сборок
    private var DNFsCounter = 0

    fun addSolve(solve: ShortSolve): Int {
        /*
        Если количество сборок пока недостаточно для подсчета среднего - просто добавляем сборку
        в список. Иначе вызываем метод для пересчета среднего.
         */
        if (solvesInAvg == 0) {
            return recalculateMean(TimeFormat.solveToResult(solve))
        }

        if (solves.size < solvesInAvg) {
            solves.add(TimeFormat.solveToResult(solve))
            sortedSolves = solves
            if (solves.size == solvesInAvg) {
                return calculate()
            }
        } else {
            return when (solvesInAvg) {
                3 -> recalculateMo3(TimeFormat.solveToResult(solve))
                else -> recalculate(TimeFormat.solveToResult(solve))
            }
        }
        return -3
    }

    private fun sort() {
        sortedSolves.sort()
    }

    private var lastStartIdx: Int = 0    // Все сборки с индексом <= этого удаляются
    private var firstEndIdx: Int = 0     // Все сборки с индексом >= этого удаляются


    fun initStat(): Int {
        solves = solvesInput.map { TimeFormat.solveToResult(it) }.toMutableList()
        if (solvesInAvg == 0) {
            lastSum = solves.sum()
            return if (solves.size == 0) {
                -3
            } else if (Int.MAX_VALUE in solves) {
                -1
            } else {
                lastSum / solves.size
            }
        }

        notCountingSolves = if (solvesInAvg == 3) 0 else ceil(solvesInAvg * 0.05).toInt()
        countingSolves = solvesInAvg - notCountingSolves * 2
        lastStartIdx = notCountingSolves - 1
        firstEndIdx = solvesInAvg - lastStartIdx - 1
        sortedSolves = this.solves.toMutableList()

        // Если мы первый раз набрали нужное количество сборок - считаем среднее, а не пересчитываем
        if (solves.size == solvesInAvg) {
            return calculate()
        }

        return -3
    }


    // Вычисление первого среднего
    private fun calculate(): Int {
        DNFsCounter = solves.count { it == Int.MAX_VALUE }
        sortedSolves = this.solves.toMutableList()
        sort()
        val filteredSolves = sortedSolves.slice(    // сборки, которые идут в зачёт
            notCountingSolves..solves.size - notCountingSolves - 1
        )
        lastSum = 0
        filteredSolves.forEach { solve -> lastSum += solve }

        if (DNFsCounter > notCountingSolves) {
            return -1
        }
        if (countingSolves != 0) {
            return lastSum / countingSolves // среднее арифмитическое
        } else {
            return -3
        }
    }


    fun recalculateMean(time: Int): Int {
        lastSum += time
        solves.add(time)
        if (Int.MAX_VALUE in solves) {
            return -1
        } else {
            return lastSum / solves.size
        }
    }


    private fun recalculateMo3(time: Int): Int {
        if (solves[0] == Int.MAX_VALUE) {
            DNFsCounter -= 1
            lastSum -= 1
        }
        if (time == Int.MAX_VALUE) {
            DNFsCounter += 1
            lastSum += 1
        } else {
            lastSum = lastSum - solves[0] + time
        }
        solves.drop(1)
        solves.add(time)

        return if (DNFsCounter > 0) -1 else lastSum / countingSolves
    }


    enum class resultType { BEST, COUNT, WORST }

    private fun recalculate(time: Int): Int {

        val outgoingSolve = solves[0]  // уходящая сборка

        if (outgoingSolve == Int.MAX_VALUE) {
            if (solvesInAvg == 5) {
                Log.d("DNF", "DNF ушел")
            }
            DNFsCounter -= 1
        }
        if (time == Int.MAX_VALUE) {
            if (solvesInAvg == 5) {
                Log.d("DNF", "DNF пришел")
            }
            DNFsCounter += 1
        }

        sort()
        Log.d("solves1", solvesInAvg.toString() + " " + sortedSolves.toString())
        val outgoingType = when {
            (outgoingSolve <= sortedSolves[lastStartIdx]) -> {
                resultType.BEST
            }

            (outgoingSolve >= sortedSolves[firstEndIdx]) -> {
                resultType.WORST
            }

            else -> {
                resultType.COUNT
            }
        }
        solves.remove(outgoingSolve)            // Удаляем уходящую сборку
        sortedSolves.remove(outgoingSolve)

        sort()

        lastSum = if (solvesInAvg == 3) {
            lastSum - outgoingSolve + time
        } else {
            if (outgoingType == resultType.BEST) {
                /*
                    На данный момент в списке solvesInAvg-1 сборок, а индексы lastStartIdx и firstEndIdx сдвигаются
                    на 1 влево, так как удаляемая сборка находится левее их обоих.
                    То есть:
                        lastStartIdx-1 — как lastStartIdx до удаления сборки
                        firstEndIdx-1 — как firstEndIdx до удаления сборки
                        lastStartIdx — индекс следующей сборки после лучших
                        firstEndIdx-2 — индекс предыдущей сборки перед хучшими

                    В остальных двух случаях (outgoingType == resultType.WORST || outgoingType == resultType.COUNT)
                    индексы считаются аналогично
                */
                if (time < sortedSolves[lastStartIdx]) {
                    // и уходящая, и приходящяя сборки относятся к лучшим -> сумма не меняется
                    lastSum
                } else if (time >= sortedSolves[firstEndIdx - 1]) {
                    lastSum - sortedSolves[lastStartIdx] + sortedSolves[firstEndIdx - 1]
                } else {
                    lastSum - sortedSolves[lastStartIdx] + time
                }
            } else if (outgoingType == resultType.WORST) {
                if (time <= sortedSolves[lastStartIdx]) {
                    lastSum - sortedSolves[firstEndIdx - 1] + sortedSolves[lastStartIdx]
                } else if (time >= sortedSolves[firstEndIdx - 1]) {
                    lastSum
                } else {
                    lastSum - sortedSolves[firstEndIdx - 1] + time
                }
            } else {
                if (time <= sortedSolves[lastStartIdx]) {
                    lastSum - outgoingSolve + sortedSolves[lastStartIdx]
                } else if (time >= sortedSolves[firstEndIdx - 1]) {
                    lastSum - outgoingSolve + sortedSolves[firstEndIdx - 1]
                } else {
                    lastSum - outgoingSolve + time
                }
            }
        }

        solves.add(time)
        sortedSolves.add(time)
        if (DNFsCounter > notCountingSolves) {
            return -1
        }

        if (countingSolves != 0) {
            return lastSum / countingSolves
        }
        return -3

    }
}