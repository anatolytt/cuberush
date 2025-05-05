package com.example.cubetime.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["sessionId", "statType", "isBestStat"])
data class Stat (
    val sessionId: Int = -1,
    val statType: Int,
    val result: Int = -3,
    val isBestStat: Boolean = false,
    val start: Int = -1
)
