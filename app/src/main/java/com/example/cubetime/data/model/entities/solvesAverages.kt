package com.example.cubetime.data.model.entities

import androidx.room.Entity
import com.example.cubetime.data.model.StatType

@Entity(primaryKeys = ["solveId", "avgType"])
data class solvesAverages(
    val solveId: Int = 0,
    val sessionId: Int,
    val avgType: StatType,
    val result: Int
)
