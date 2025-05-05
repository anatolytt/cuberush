package com.example.cubetime.data.model

import com.example.cubetime.R
import com.example.cubetime.data.model.Events.BF333
import com.example.cubetime.data.model.Events.BF444
import com.example.cubetime.data.model.Events.BF555
import com.example.cubetime.data.model.Events.CLOCK
import com.example.cubetime.data.model.Events.CUBE222
import com.example.cubetime.data.model.Events.CUBE333
import com.example.cubetime.data.model.Events.CUBE444
import com.example.cubetime.data.model.Events.CUBE555
import com.example.cubetime.data.model.Events.CUBE666
import com.example.cubetime.data.model.Events.CUBE777
import com.example.cubetime.data.model.Events.MBLD
import com.example.cubetime.data.model.Events.MEGA
import com.example.cubetime.data.model.Events.ONE_HANDED
import com.example.cubetime.data.model.Events.PYRA
import com.example.cubetime.data.model.Events.SKEWB
import com.example.cubetime.data.model.Events.SQ1

enum class StatType {
    SINGLE,
    MEAN,
    MO3,
    AO5,
    AO12,
    AO25,
    AO50,
    AO100;

    fun getSolvesInAvg() : Int {
        return when(this) {
            SINGLE -> 1
            MEAN -> 0
            MO3 -> 3
            AO5 -> 5
            AO12 -> 12
            AO25 -> 25
            AO50 -> 50
            AO100 -> 100
        }
    }

    fun getName() : String {
        return when(this) {
            SINGLE -> "Single"
            MEAN -> "Mean"
            MO3 -> "Mo3"
            AO5 -> "Ao5"
            AO12 -> "Ao12"
            AO25 -> "Ao25"
            AO50 -> "Ao50"
            AO100 -> "Ao100"
        }
    }
}