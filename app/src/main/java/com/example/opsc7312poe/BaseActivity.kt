package com.example.opsc7312poe

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc7312poe.utils.LocaleHelper

open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        // Get the persisted language setting
        val localeHelper = LocaleHelper()
        val language = localeHelper.getPersistedLanguage(newBase) ?: "en"

        // Apply the locale to the context
        val context = localeHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}