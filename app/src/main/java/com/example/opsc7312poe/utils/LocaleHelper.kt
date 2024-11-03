package com.example.opsc7312poe.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

class LocaleHelper {

    private val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    // Set the locale and persist the choice
    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else {
            updateResourcesLegacy(context, language)
        }
    }

    // Save the selected language in a dedicated SharedPreferences file
    private fun persist(context: Context, language: String) {
        val preferences: SharedPreferences = context.getSharedPreferences("LanguagePrefs", Context.MODE_PRIVATE)
        preferences.edit().putString(SELECTED_LANGUAGE, language).apply()
    }

    // Retrieve the persisted language
    fun getPersistedLanguage(context: Context): String? {
        val preferences: SharedPreferences = context.getSharedPreferences("LanguagePrefs", Context.MODE_PRIVATE)
        return preferences.getString(SELECTED_LANGUAGE, Locale.getDefault().language)
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    // Update resources for devices running below Android N
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val config = Configuration(resources.configuration)
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        return context
    }
}
