package com.example.cubetime.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Best_averages(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val sessionId: Int,
    val mo3: Int = 0,
    val ao5: Int = 0,
    val ao12: Int = 0,
    val ao25: Int = 0,
    val ao50: Int = 0,
    val ao100: Int = 0


)
