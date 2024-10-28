package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    private val chatFragment = ChatFragment()
    private val notificationFragment = NotificationFragment()
    private val homeFragment = HomeFragment()
    private val calendarFragment = CalendarFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_home)

        if (savedInstanceState == null) {
            // Begin the fragment transaction to display the HomeFragment
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, homeFragment) // R.id.frame_layout is your FrameLayout container in activity_home.xml
            fragmentTransaction.commit() // Commit the transaction
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(homeFragment)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> replaceFragment(homeFragment)
                R.id.Chat -> replaceFragment(chatFragment)
                R.id.Settings -> {
                    // Start the Settings activity instead of replacing a fragment
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                }
                R.id.Notification -> replaceFragment(notificationFragment)
                R.id.Calender -> replaceFragment(calendarFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}
