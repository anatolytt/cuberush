package com.example.cubetime.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun ChangeLanguage(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration()
    config.setLocale(locale)

    context.resources.updateConfiguration(config, context.resources.displayMetrics)

}