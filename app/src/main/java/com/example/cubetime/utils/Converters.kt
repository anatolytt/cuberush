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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    @TypeConverter
    fun dateFromString(value: String): Date? {
        return dateFormatter.parse(value)
    }

    @TypeConverter
    fun dateToString(date: Date): String? {
        return dateFormatter.format(date)
    }

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
}