package com.example.cubetime.model

import com.example.cubetime.utils.Scrambler

data class Solve(
    val result: Double,
    val event: Events,
    val penalties: Penalties,
    val date: String, // потом поменяем
    val scrambler: String,
    val comment: String,
    val reconstruction: String,
    val isCustomScramble: Boolean
)
