package com.example.cubetime.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val event: Events,
    val createDate : String? = null
)
