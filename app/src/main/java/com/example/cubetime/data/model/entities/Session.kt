package com.example.cubetime.data.model.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.cubetime.data.model.Events

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val event: Events,
    val createDate : String? = null,
    val bestSingle: Int = 0,
    val bestAo5: Int = 0,
    val bestAo12: Int = 0,
    val bestAo25: Int = 0,
    val bestAo50: Int = 0,
    val bestAo100: Int = 0,
    val bestAo1000: Int = 0
)
