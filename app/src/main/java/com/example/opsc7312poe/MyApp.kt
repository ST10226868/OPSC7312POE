package com.example.opsc7312poe

import android.app.Application
import com.example.opsc7312poe.utils.HighScoreManager
import com.example.opsc7312poe.utils.LocaleHelper

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        HighScoreManager.init(this)

        val localeHelper = LocaleHelper()
        val language = localeHelper.getPersistedLanguage(this) ?: "en"
        localeHelper.setLocale(this, language)
    }
}
