package com.example.opsc7312poe

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.opsc7312poe.utils.HighScoreManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SnakeHomeActivity : BaseActivity() {

    private lateinit var highScoreNum : TextView
    private lateinit var playBtn: Button
    private lateinit var leaderboardBtn: Button
    private lateinit var restBtn : Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snake_home)

        playBtn = findViewById(R.id.playButton)
        leaderboardBtn = findViewById(R.id.LeaderboardButton)
        restBtn = findViewById(R.id.RestButton)
        backBtn = findViewById(R.id.BackButton)
        highScoreNum = findViewById(R.id.scoreNum)

        HighScoreManager.init(this)
        highScoreNum.text = HighScoreManager.getHighScore().toString()


        playBtn.setOnClickListener { navigateToSnakeGame() }
        leaderboardBtn.setOnClickListener { navigateToLeaderboard() }
        restBtn.setOnClickListener { deleteScore() }
        backBtn.setOnClickListener { navigateToSettings() }
    }



    private fun navigateToSnakeGame() {
        val intent = Intent(this, SnakeGameActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun deleteScore() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Delete")
            .setMessage("Select an option to clear your high score:")
            .setPositiveButton("Clear Local Score") { dialog, which ->
                // Clear local high score from SharedPreferences
                val sharedPrefs = getSharedPreferences("SnakeGamePrefs", Context.MODE_PRIVATE)
                sharedPrefs.edit().remove("high_score").apply()
                Toast.makeText(this, "Local high score cleared.", Toast.LENGTH_SHORT).show()

                // Update displayed high score to 0
                highScoreNum.text = "0"
            }
            .setNeutralButton("Clear Online Score") { dialog, which ->
                if (isOnline()) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val db = FirebaseFirestore.getInstance()
                        val leaderboardRef = db.collection("Leaderboard").document(userId)

                        leaderboardRef.update("highScore", 0)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Online high score cleared and set to 0.", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to update Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                e.printStackTrace()
                            }
                    } else {
                        Toast.makeText(this, "User not logged in, unable to clear online score.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "You are offline. Cannot clear online score.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Clear Both") { dialog, which ->
                // Clear local score
                val sharedPrefs = getSharedPreferences("SnakeGamePrefs", Context.MODE_PRIVATE)
                sharedPrefs.edit().remove("high_score").apply()
                Toast.makeText(this, "Local high score cleared.", Toast.LENGTH_SHORT).show()

                // Update displayed high score to 0
                highScoreNum.text = "0"

                // Attempt to clear online score
                if (isOnline()) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val db = FirebaseFirestore.getInstance()
                        val leaderboardRef = db.collection("Leaderboard").document(userId)

                        leaderboardRef.update("highScore", 0)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Online high score cleared and set to 0.", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to update Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                e.printStackTrace()
                            }
                    } else {
                        Toast.makeText(this, "User not logged in, unable to clear scores.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "You are offline. Cannot clear online score.", Toast.LENGTH_SHORT).show()
                }
            }
            .setCancelable(true)
            .show()
    }


    private fun isOnline(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    private fun navigateToLeaderboard() {
        if (isOnline()) {
            val intent = Intent(this, Leaderboard::class.java)
            startActivity(intent)
            this.finish()
        } else {
            Toast.makeText(this, "Unavailable offline", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToSettings() {
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
        this.finish()
    }
}