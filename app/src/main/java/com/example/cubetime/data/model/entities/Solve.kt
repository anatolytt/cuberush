package com.example.cubetime.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.Penalties

@Entity
data class Solve (
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    val sessionId : Int,
    val result: Int,
    val event: Events,
    val penalties: Penalties,
    val date: String,
    val scramble: String,
    val comment: String,
    val reconstruction: String,
    val isCustomScramble: Boolean
)
