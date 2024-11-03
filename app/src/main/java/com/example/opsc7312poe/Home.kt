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
    private var homeFragment = HomeFragment()
   // private val calendarFragment = CalendarFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_home)

        // Get the email passed from LoginActivity
        val email = intent.getStringExtra("email") ?: "Guest" // Default to "Guest" if not found

        // Initialize HomeFragment and pass the email
        homeFragment = HomeFragment().apply {
            arguments = Bundle().apply {
                putString("email", email) // Pass the email to HomeFragment
            }
        }

        if (savedInstanceState == null) {
            // Begin the fragment transaction to display the HomeFragment
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, homeFragment) // R.id.frame_layout is your FrameLayout container in activity_home.xml
            fragmentTransaction.commit() // Commit the transaction
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        when (this::class.java) {
            Home::class.java -> bottomNav.menu.findItem(R.id.Home).isChecked = true
        }


        if (savedInstanceState == null) {
            replaceFragment(homeFragment)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Home -> {
                    replaceFragment(homeFragment)
                    true
                }
                R.id.Chat -> {
                    replaceFragment(chatFragment)
                    true
                }
                R.id.Notification -> {
                    replaceFragment(notificationFragment)
                    true
                }
                R.id.Calender -> {
                    //calanderFragment (Fragment didnt exist)
                    replaceFragment(notificationFragment)
                    true
                }
                R.id.Settings -> {
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                    false
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}

