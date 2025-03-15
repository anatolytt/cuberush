package com.example.cubetime.utils

object TimeFormat {
    fun millisToString (millis : Int) : String {
        if (millis < 60000) {
            return (String.format("%.2f", (millis.toDouble())/1000))
        } else {
            val minutes = millis / 60000
            val millisLeft = millis % 60000
            val milliseconds = millisLeft % 1000
            var seconds = (millisLeft / 1000).toString()
            if (seconds.length == 1) { seconds = "0" + seconds }
            return ("${minutes}:${seconds}.${milliseconds}")
        }
    }
}