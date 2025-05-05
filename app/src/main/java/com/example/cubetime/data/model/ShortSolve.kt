package com.example.cubetime.data.model

data class ShortSolve (
    val id: Int,
    val result: Int,
    val penalties: Penalties,
    val date: String = ""
) {}
