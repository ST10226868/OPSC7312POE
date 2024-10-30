package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class Settings : AppCompatActivity() {

    private lateinit var pfpImage: ImageView
    private lateinit var accountBtn: Button
    private lateinit var passwordChangeBtn: Button
    private lateinit var snakeBtn: Button
    private lateinit var notificationBtn: Button
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
        notificationBtn = findViewById(R.id.NotificationBtn)
        languageBtn = findViewById(R.id.LanguageBtn)
        backBtn = findViewById(R.id.backBtn)
        signOutBtn = findViewById(R.id.signOut)

        loadProfilePicture()

        accountBtn.setOnClickListener { navigateToAccountSettings() }
        passwordChangeBtn.setOnClickListener { navigateToPasswordChange() }
        snakeBtn.setOnClickListener { navigateToSnakeHome() }
        notificationBtn.setOnClickListener { showToast("Coming soon") }
        languageBtn.setOnClickListener { showToast("Coming soon") }
        signOutBtn.setOnClickListener { logoutAndCloseApp() }

        backBtn.setOnClickListener {
            val intent = Intent(this, HomeFragment::class.java)
            startActivity(intent)
            finish()
        }
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
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val userProfilePictureRef = FirebaseStorage.getInstance().reference.child("profile_pictures/$userId.png")

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



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun logoutAndCloseApp() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        finishAffinity() // Close the app
    }
}