package com.example.opsc7312poe

import android.app.Application
import android.util.Log
import com.example.opsc7312poe.utils.HighScoreManager
import com.example.opsc7312poe.utils.LocaleHelper
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.FirebaseApp

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize high score manager and language settings
        HighScoreManager.init(this)
        val localeHelper = LocaleHelper()
        val language = localeHelper.getPersistedLanguage(this) ?: "en"
        localeHelper.setLocale(this, language)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Retrieve and log the FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Get the FCM token
                val token = task.result
                Log.d("FCM Token", "FCM Token: $token")
            } else {
                Log.w("FCM Token", "Fetching FCM registration token failed", task.exception)
            }
        }
    }
}
