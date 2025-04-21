package com.example.cubetime.utils

import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.Solve
import kotlin.math.ceil

class StatisticsCalculator (
){

/*
    Храним и список со всеми сборками, и список с отсорированными сборкамиб чтобы не сортировать
    список с нуля при каждом добавлении сборки.
 */
    lateinit private var solves: MutableList<Int>
    lateinit private var sortedSolves : MutableList<Int>

    private var lastSum: Int = 0    // Сумма сборок в зачёт последнего подсчитанного среднего
    private var solvesInAvg: Int = 0    // Всего сборок в среднем

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
            return recalculate(solve.result)
        }

        return 0
    }

    private fun sort() {
        sortedSolves.sort()
    }

    private var lastStartIdx: Int = 0    // Все сборки с индексом <= этого удаляются
    private var firstEndIdx: Int = 0     // Все сборки с индексом >= этого удаляются

    fun initStat(
        solves: MutableList<ShortSolve>,
        solvesInAvg: Int
    ) : Int {
        this.solves = solves.map {
            when (it.penalties) {
                Penalties.NONE -> it.result
                Penalties.PLUS2 -> it.result + 2000
                Penalties.DNF -> Int.MAX_VALUE
            }
        }.toMutableList()
        this.solvesInAvg = solvesInAvg
        notCountingSolves = ceil(solvesInAvg * 0.05).toInt()   // округление вверх
        countingSolves = solvesInAvg - notCountingSolves*2
        lastStartIdx = notCountingSolves - 1
        firstEndIdx = solvesInAvg - lastStartIdx - 1
        sortedSolves = this.solves

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
            }
        }
        lastSum = sum
        if (countingDnfCount) {
            return -1   //  если среднее - днф
        }
        return sum / countingSolves     // среднее арифмитическое
    }


    enum class resultType {BEST, COUNT, WORST}
    private fun recalculate(time:Int) : Int {
        val solve = time
        val average: Int    // итоговое среднее
        val outgoingSolve = solves[0]  // уходящая сборка
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

        // Удаляем уходящую сборку
        sortedSolves.remove(solves[0])
        solves.removeAt(0)
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
            if (solve <= sortedSolves[lastStartIdx-1]) {
                // и уходящая, и приходящяя сборки относятся к лучшим -> сумма не меняется
                lastSum
            } else if (solve >= sortedSolves[firstEndIdx-1]) {
                lastSum - sortedSolves[lastStartIdx] + sortedSolves[firstEndIdx-2]
            } else {
                lastSum - sortedSolves[lastStartIdx] + solve
            }
        } else if (outgoingType == resultType.WORST) {
            if (solve <= sortedSolves[lastStartIdx]) {
                lastSum - sortedSolves[firstEndIdx-1] + sortedSolves[lastStartIdx]
            } else if (solve >= sortedSolves[firstEndIdx]) {
                lastSum
            } else {
                lastSum - solves[firstEndIdx] + solve
            }
        } else {
            if (solve <= sortedSolves[lastStartIdx]) {
                lastSum - outgoingSolve + sortedSolves[lastStartIdx]
            } else if (solve >= sortedSolves[firstEndIdx]) {
                lastSum - outgoingSolve + sortedSolves[firstEndIdx]
            } else {
                lastSum - outgoingSolve + solve
            }
        }
        solves.add(solve)
        return lastSum
    }
}