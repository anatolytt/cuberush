package com.example.cubetime.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["solveId", "avgType"])
data class solvesAverages(
    val solveId: Int = 0,
    val sessionId: Int,
    val avgType: StatType,
    val result: Int
)
