package com.example.cubetime.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Solve(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    val sessionName : String,
    val result: Int,
    val event: Events,
    val penalties: Penalties,
    val date: String,
    val scramble: String,
    val comment: String,
    val reconstruction: String,
    val isCustomScramble: Boolean
)
