package com.example.cubetime

import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.utils.statistics.StatisticsCalculator
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class StatCalculatorTest {
    val SOLVES_IN_AVG = 12
    val testCalculator = StatsTestClass()
    val resultList = mutableListOf(2360, 9840, 4520, 2340, 8530, 1240, 2340, 6580, 2340, 6780, 2140, 9877)
    val shortSolveList = mutableListOf<ShortSolve>()
    init {
        resultList.forEach { solve -> shortSolveList.add(ShortSolve(0, solve, Penalties.NONE, "")) }
    }

    @Test
    fun statCalculatorTest()  {
        val calculator = StatisticsCalculator( shortSolveList.toList(), 12)
        assertEquals(testCalculator.calculate(12, resultList), calculator.initStat())
        assertEquals(resultList.addResult(800), calculator.addSolve(ShortSolve(0, 800, Penalties.NONE, "")))
        assertEquals(resultList.addResult(800), calculator.addSolve(ShortSolve(0, 800, Penalties.NONE, "")))
        assertEquals(resultList.addResult(5230), calculator.addSolve(ShortSolve(0, 5230, Penalties.NONE, "")))
        assertEquals(resultList.addResult(3540), calculator.addSolve(ShortSolve(0, 1540, Penalties.PLUS2, "")))
        assertEquals(resultList.addResult(Int.MAX_VALUE), calculator.addSolve(ShortSolve(0, 1540, Penalties.DNF, "")))
        assertEquals(resultList.addResult(7820), calculator.addSolve(ShortSolve(0, 7820, Penalties.NONE, "")))
        assertEquals(-1, calculator.addSolve(ShortSolve(0, 7820, Penalties.DNF, "")))

    }

    private fun MutableList<Int>.addResult(solve: Int): Int {
        this.add(solve)
        this.removeAt(0)
        return testCalculator.calculate(12, this)
    }
}