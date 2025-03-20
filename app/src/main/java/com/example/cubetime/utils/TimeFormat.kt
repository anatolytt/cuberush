package com.example.cubetime.utils

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import java.util.Locale

object TimeFormat {
    fun millisToString(millis: Int): String {
        if (millis < 60000) {
            return (String.format(Locale.US, "%.2f", (millis.toDouble()) / 1000))
        } else {
            val minutes = millis / 60000
            val millisLeft = millis % 60000
            val milliseconds = millisLeft % 1000 / 10
            var seconds = (millisLeft / 1000).toString()
            if (seconds.length == 1) {
                seconds = "0" + seconds
            }
            return ("${minutes}:${seconds}.${milliseconds}")
        }
    }

    fun inputTextToMillis(input: String): Int {
        val inputText = input.padStart(6, '0')
        val minutes = inputText.slice(0 .. 1).toInt()
        val seconds = inputText.slice(2 .. 3).toInt()
        val milliseconds = inputText.slice(4 .. 5).toInt()
        return milliseconds*10 + seconds*1000 + minutes*60000
    }


    fun inputTextVisualTransformation(input: AnnotatedString): TransformedText {
        val text = if (input.text.length > 6) input.text.takeLast(6) else input.text.padStart(6, '0')
        val result = "${text.substring(0 .. 1)}:${text.substring(2..3)}.${text.substring(4..5)}"

        val indicesOffsetTranslator = object: OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return 8
            }
            override fun transformedToOriginal(offset: Int): Int {
                return 0
            }
        }
        return TransformedText(AnnotatedString(result), indicesOffsetTranslator)
    }
}