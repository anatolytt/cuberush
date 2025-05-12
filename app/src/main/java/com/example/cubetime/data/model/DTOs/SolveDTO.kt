package com.example.cubetime.data.model.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class SolveDTO(
    val result: Int,
    val scramble: String,
    val penalty: Int,
    val comment: String = "",
    val date: String
)
