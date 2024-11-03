package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


class Settings : BaseActivity() {

    private lateinit var pfpImage: ImageView
    private lateinit var accountBtn: Button
    private lateinit var passwordChangeBtn: Button
    private lateinit var snakeBtn: Button
    private lateinit var languageBtn: Button
    private lateinit var signOutBtn: Button
    private lateinit var backBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pfpImage = findViewById(R.id.pfpImage)
        accountBtn = findViewById(R.id.AccountBtn)
        passwordChangeBtn = findViewById(R.id.PasswordChangeBtn)
        snakeBtn = findViewById(R.id.snakeBtn)
        languageBtn = findViewById(R.id.LanguageBtn)
        backBtn = findViewById(R.id.backBtn)
        signOutBtn = findViewById(R.id.signOut)

        loadProfilePicture()

        accountBtn.setOnClickListener { navigateToAccountSettings() }
        passwordChangeBtn.setOnClickListener { navigateToPasswordChange() }
        snakeBtn.setOnClickListener { navigateToSnakeHome() }
        languageBtn.setOnClickListener { navigateToLanguagePage() }
        signOutBtn.setOnClickListener { logoutAndCloseApp() }

        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun navigateToLanguagePage() {
        val intent = Intent(this, Language::class.java)
        startActivity(intent)
        finish()
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun navigateToSnakeHome() {
        val intent = Intent(this, SnakeHomeActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun navigateToAccountSettings() {
        val intent = Intent(this, AccountSettings::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToPasswordChange() {
        val intent = Intent(this, PasswordChange::class.java)
        startActivity(intent)
    }


    private fun loadProfilePicture() {
        if (!isOnline(this)) {
            // Device is offline - show offline indicator on profile picture
            pfpImage.setImageResource(R.drawable.offline_placeholder)
            Toast.makeText(this, "You're offline.", Toast.LENGTH_LONG).show()
            return
        }
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val userProfilePictureRef = FirebaseStorage.getInstance().reference.child("profile_pictures/$userId.jpg")

            userProfilePictureRef.downloadUrl.addOnSuccessListener { uri ->
                Picasso.get()
                    .load(uri)
                    .into(findViewById<ImageView>(R.id.pfpImage))
            }.addOnFailureListener {
                val defaultProfilePictureRef = FirebaseStorage.getInstance().reference.child("no_pfp.png")
                defaultProfilePictureRef.downloadUrl.addOnSuccessListener { uri ->
                    Picasso.get()
                        .load(uri)
                        .into(findViewById<ImageView>(R.id.pfpImage))
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to load profile picture: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }


    private fun logoutAndCloseApp() {
        clearHighScore()
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        finishAffinity() // Close the app
    }

    // Clear high score from SharedPreferences
    private fun clearHighScore() {
        val sharedPrefs = getSharedPreferences("SnakeGamePrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().remove("high_score").apply()
        Toast.makeText(this, "High score cleared.", Toast.LENGTH_SHORT).show()
    }
}