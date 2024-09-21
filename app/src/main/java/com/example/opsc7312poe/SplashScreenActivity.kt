package com.example.opsc7312poe


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Simulate a delay to show splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate to LoginActivity after the delay
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Close the SplashActivity
        }, 3000) // 3 seconds delay
    }
}
