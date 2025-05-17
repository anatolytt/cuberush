package com.example.cubetime.data.model.DTOs

import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.Penalties
import kotlinx.serialization.Serializable

@Serializable
data class SolveDTO(
    val result: Int,
    val scramble: String,
    val penalty: Penalties,
    val comment: String = "",
    val date: String,
    val event: Events
)
