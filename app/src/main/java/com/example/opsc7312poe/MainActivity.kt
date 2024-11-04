package com.example.opsc7312poe

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // On Register button click, go to RegistrationActivity
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        // On Login button click, go to LoginActivity
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Request notification permission for Android 13+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted, request it
                requestNotificationPermission()
            } else {
                // Permission already granted, enable notifications
                setNotificationPreference(true)
            }
        } else {
            setNotificationPreference(true)
        }
    }

    private fun requestNotificationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Update notification preference based on permission result
        setNotificationPreference(isGranted)
    }

    private fun setNotificationPreference(isEnabled: Boolean) {
        // Save the user's choice in SharedPreferences
        val sharedPref = getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("notifications_enabled", isEnabled)
            apply()
        }
    }
}
