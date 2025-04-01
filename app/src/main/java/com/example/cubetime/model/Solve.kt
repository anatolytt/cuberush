package com.example.cubetime.model

import com.example.cubetime.utils.Scrambler

data class Solve(
    var id:String,
    val result: Int,
    val event: Events,
    val penalties: Penalties,
    val date: String, // потом поменяем
    val scramble: String,
    val comment: String,
    val reconstruction: String,
    val isCustomScramble: Boolean
)
