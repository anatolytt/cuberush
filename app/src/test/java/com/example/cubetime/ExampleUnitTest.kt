package com.example.cubetime

import android.util.Log
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.utils.StatisticsCalculator
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun statManagerTest() {
        val calculator = StatisticsCalculator()
        val initAvg = calculator.initStat(
            mutableListOf(
                ShortSolve(0, 5300, Penalties.NONE, ""),
                ShortSolve(0, 5500, Penalties.NONE, ""),
                ShortSolve(0, 1000, Penalties.NONE, ""),
                ShortSolve(0, 8903, Penalties.NONE, ""),
                ShortSolve(0, 1200, Penalties.NONE, ""),
            ),
            5
        )


        assertEquals(4000, calculator.addSolve(ShortSolve(0, 5300, Penalties.NONE, "")))
    }
}