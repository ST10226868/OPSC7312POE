package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.opsc7312poe.utils.LocaleHelper

class Language : BaseActivity() {

    private lateinit var englishBtn: Button
    private lateinit var isiZuluBtn: Button
    private lateinit var afrikaansBtn: Button
    private lateinit var xhosaBtn: Button
    private lateinit var translatorBtn: Button
    private lateinit var backBtn: Button
    private lateinit var localeHelper: LocaleHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        //Local Language assistent
        localeHelper = LocaleHelper()

        englishBtn = findViewById(R.id.EnglishBtn)
        backBtn = findViewById(R.id.backBtn)
        isiZuluBtn = findViewById(R.id.IsiZuluBtn)
        afrikaansBtn = findViewById(R.id.AfrikaansBtn)
        xhosaBtn = findViewById(R.id.XhosaBtn)
        translatorBtn = findViewById(R.id.TranslatorBtn)

        backBtn.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }

        englishBtn.setOnClickListener {
            changeLanguage("en")
        }
        isiZuluBtn.setOnClickListener {
            changeLanguage("zu")
        }
        afrikaansBtn.setOnClickListener {
            changeLanguage("af")
        }
        xhosaBtn.setOnClickListener {
            changeLanguage("xh")
        }


        translatorBtn.setOnClickListener {
            val intent = Intent(this, Translator::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun changeLanguage(languageCode: String) {
        localeHelper.setLocale(this, languageCode)
        recreate()
    }
}