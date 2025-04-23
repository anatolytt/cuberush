package com.example.cubetime

import com.example.cubetime.data.model.ShortSolve
import kotlin.math.ceil

class StatsTestClass {
    fun calculate(n: Int, solves: List<Int>) : Int{
        val sorted = solves.sorted()
        val notCountingSolves = ceil(n * 0.05).toInt()
        val filteredSolves = sorted.slice(
            notCountingSolves..solves.size - notCountingSolves - 1
        )
        var sum = 0
        filteredSolves.forEach { solve ->
            sum += solve
        }

        return sum / (n - 2*notCountingSolves)

    }
}