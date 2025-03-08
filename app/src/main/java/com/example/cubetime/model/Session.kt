package com.example.cubetime.model

data class Session(
    val name: String,
    val event: Events,
    val createDate : String? = null,
    val id: Int? = null
)
