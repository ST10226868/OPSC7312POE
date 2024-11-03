package com.example.opsc7312poe

import android.app.Application
import com.example.opsc7312poe.utils.HighScoreManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        HighScoreManager.init(this)
    }
}
