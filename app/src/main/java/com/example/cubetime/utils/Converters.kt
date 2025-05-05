package com.example.cubetime.utils

import androidx.room.TypeConverter
import com.example.cubetime.R
import com.example.cubetime.data.model.Events
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
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.StatType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {
    @TypeConverter
    fun eventToString(event: Events) : String? {
        return event.toString()
    }

    @TypeConverter
    fun eventFromString(string: String) : Events {
        return when(string) {
            "CUBE222" -> Events.CUBE222
            "CUBE333" -> Events.CUBE333
            "CUBE444" -> Events.CUBE444
            "CUBE555" -> Events.CUBE555
            "CUBE666" ->  Events.CUBE666
            "CUBE777" ->  Events.CUBE777
            "ONE_HANDED" ->  Events.ONE_HANDED
            "PYRA" ->  Events.PYRA
            "SKEWB" -> Events.SKEWB
            "SQ1" -> Events.SQ1
            "CLOCK" -> Events.CLOCK
            "MEGA" -> Events.MEGA
            "BF333" -> Events.BF333
            "BF444" ->  Events.BF444
            "BF555" ->  Events.BF555
            "MBLD" -> Events.MBLD
            else -> {
                Events.CUBE333
            }
        }
    }

    @TypeConverter
    fun penaltyToInt(penalty: Penalties) : Int {
        return when (penalty) {
            Penalties.NONE -> 0
            Penalties.DNF -> -1
            Penalties.PLUS2 -> 1
        }
    }

    @TypeConverter
    fun intToPenalty(int: Int) : Penalties{
        return when (int) {
            0 -> Penalties.NONE
            1 -> Penalties.PLUS2
            -1 -> Penalties.DNF
            else -> Penalties.NONE
        }
    }

    @TypeConverter
    fun statTypeToInt(statType: StatType) : Int {
        return when (statType) {
            StatType.MEAN -> 0
            StatType.SINGLE -> 1
            StatType.MO3 -> 3
            StatType.AO5 -> 5
            StatType.AO12 -> 12
            StatType.AO25 -> 25
            StatType.AO50 -> 50
            StatType.AO100 -> 100
        }
    }

    @TypeConverter
    fun intToStatType(int: Int) : StatType {
        return when (int) {
            0 -> StatType.MEAN
            1 -> StatType.SINGLE
            3 -> StatType.MO3
            5 -> StatType.AO5
            12 -> StatType.AO12
            25 -> StatType.AO25
            50 -> StatType.AO50
            else -> StatType.MO3
        }
    }
}