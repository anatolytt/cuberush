package com.example.cubetime.utils.statistics

import android.util.Log
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.ShortSolve
import kotlin.math.ceil

class StatisticsCalculator (
    var solvesInput: List<ShortSolve>,
    var solvesInAvg: Int // Всего сборок в среднем
)
{
/*
    Храним и список со всеми сборками, и список с отсорированными сборкамиб чтобы не сортировать
    список с нуля при каждом добавлении сборки.
 */
    lateinit private var solves: MutableList<Int>
    lateinit private var sortedSolves : MutableList<Int>


    private var lastSum: Int = 0    // Сумма сборок в зачёт последнего подсчитанного среднего

    // Количествос сборок, не идущих в зачет с каждой стороны
    private var notCountingSolves: Int = 0
    // Количество сборок, идущих в зачет
    private var countingSolves: Int = 0

    // Количество днфов в текущем списке сборок
    private var DNFsCount = 0

    fun addSolve(solve: ShortSolve) : Int{
        /*
        Если количество сборок пока недостаточно для подсчета среднего - просто добавляем сборку
        в список. Иначе вызываем метод для пересчета среднего.
         */
        if (solves.size < solvesInAvg-1) {
            solves.add(solve.result)
        } else {
            return recalculate(solveToResult(solve))
        }
        return 0
    }

    private fun sort() { sortedSolves.sort() }

    private var lastStartIdx: Int = 0    // Все сборки с индексом <= этого удаляются
    private var firstEndIdx: Int = 0     // Все сборки с индексом >= этого удаляются

    fun initStat() : Int {
        this.solves = solvesInput.map { solveToResult(it) }.toMutableList()
        notCountingSolves = ceil(solvesInAvg * 0.05).toInt()   // округление вверх
        countingSolves = solvesInAvg - notCountingSolves*2
        lastStartIdx = notCountingSolves - 1
        firstEndIdx = solvesInAvg - lastStartIdx - 1
        sortedSolves = this.solves.toMutableList()

        // Если мы первый раз набрали нужное количество сборок - считаем среднее, а не пересчитываем
            if (solves.size == solvesInAvg) {
                return calculate()
            }
        return 0
    }


    // Вычисление первого среднего
    private fun calculate() : Int {
        sort()
        val filteredSolves = sortedSolves.slice(    // сборки, которые идут в зачёт
            notCountingSolves..solves.size - notCountingSolves - 1
        )
        var sum = 0
        var countingDnfCount: Boolean = false
        filteredSolves.forEach { solve ->
            sum += solve
            if (solve == Int.MAX_VALUE) {
                countingDnfCount = true
                DNFsCount += 1
            }
        }
        lastSum = sum
        if (countingDnfCount) {
            return -1   //  если среднее - днф
        }
        return sum / countingSolves // среднее арифмитическое
    }


    enum class resultType {BEST, COUNT, WORST}
    private fun recalculate(time:Int) : Int {
        val solve = time
        val outgoingSolve = solves[0]  // уходящая сборка
        sort()
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
        if (outgoingType == resultType.COUNT && outgoingSolve == Int.MAX_VALUE) { DNFsCount -= 1 }

        // Удаляем уходящую сборку
        solves.remove(outgoingSolve)
        sortedSolves.remove(outgoingSolve)
        sortedSolves.size
        solves.size
        solves
        sort()

        lastSum = if (outgoingType == resultType.BEST) {
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
            if (solve < sortedSolves[lastStartIdx]) {
                // и уходящая, и приходящяя сборки относятся к лучшим -> сумма не меняется
                lastSum
            } else if (solve >= sortedSolves[firstEndIdx-1]) {
                lastSum - sortedSolves[lastStartIdx] + sortedSolves[firstEndIdx-1]
            } else {
                if (solve == Int.MAX_VALUE) { DNFsCount += 1 }
                lastSum - sortedSolves[lastStartIdx] + solve
            }
        } else if (outgoingType == resultType.WORST) {
            if (solve <= sortedSolves[lastStartIdx]) {
                lastSum - sortedSolves[firstEndIdx-1] + sortedSolves[lastStartIdx]
            } else if (solve >= sortedSolves[firstEndIdx]) {
                lastSum
            } else {
                if (solve == Int.MAX_VALUE) { DNFsCount += 1 }
                lastSum - solves[firstEndIdx] + solve
            }
        } else {
            if (solve <= sortedSolves[lastStartIdx]) {
                lastSum - outgoingSolve + sortedSolves[lastStartIdx]
            } else if (solve >= sortedSolves[firstEndIdx-1]) {
                lastSum - outgoingSolve + sortedSolves[firstEndIdx-1]
            } else {
                if (solve == Int.MAX_VALUE) { DNFsCount += 1 }
                lastSum - outgoingSolve + solve
            }
        }
        solves.add(solve)
        sortedSolves.add(solve)
        if (DNFsCount >= 1) {
            return -1
        } else {
            return lastSum / countingSolves
        }
    }

    private fun solveToResult(solve: ShortSolve): Int {
        return when (solve.penalties) {
            Penalties.NONE -> solve.result
            Penalties.PLUS2 -> solve.result + 2000
            Penalties.DNF -> Int.MAX_VALUE
        }
    }
}