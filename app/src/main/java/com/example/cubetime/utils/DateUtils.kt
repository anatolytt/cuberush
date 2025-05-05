package com.example.cubetime.utils

import com.example.cubetime.shared.AppStrings
import java.time.LocalDate

object DateUtils {
    fun getCurrentDate() : String{
        val currentDate = LocalDate.now()
        val del = AppStrings.dateDelimiter
        return "${currentDate.dayOfMonth.toString().padStart(2, '0')}$del${currentDate.monthValue.toString().padStart(2, '0')}$del${currentDate.year}"
    }
}