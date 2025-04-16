package com.example.cubetime.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val event: Events,
    val createDate : String? = null
)
