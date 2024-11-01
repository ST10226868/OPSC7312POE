package com.example.opsc7312poe.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object HighScoreManager {
    private var currentScore: Int = 0
    private var highScore: Int = 0
    private var pendingHighScore: Int? = null

    // Initialize the high score from SharedPreferences
    fun init(context: Context) {
        highScore = getHighScoreFromPreferences(context)
    }

    // Update the current score
    fun updateCurrentScore(score: Int) {
        currentScore = score
    }

    // Retrieve the current high score
    fun getHighScore(): Int {
        return highScore
    }

    // Set a new high score if it's greater than the existing high score
    fun setHighScore(context: Context, newScore: Int) {
        if (newScore > highScore) {
            highScore = newScore
            saveHighScoreToPreferences(context, highScore)
            pendingHighScore = highScore
            syncHighScoreWithFirebaseIfNeeded(context)
        }
    }

    // Sync high score to Firebase if the device is online and there’s a new high score to sync
    fun syncHighScoreWithFirebaseIfNeeded(context: Context) {
        if (pendingHighScore != null) {
            if (isOnline(context)) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
                val db = FirebaseFirestore.getInstance()

                // Fetch the username from the users collection
                db.collection("users").document(userId).get().addOnSuccessListener { userDocument ->
                    val username = userDocument.getString("name") ?: "Unknown" // Get the username from the document
                    saveHighScoreToFirebase(userId, username, pendingHighScore, context)
                }.addOnFailureListener { e ->
                    e.printStackTrace()
                    // Handle the error case if fetching the username fails
                    Toast.makeText(context, "Failed to retrieve username.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Notify user about offline saving
                Toast.makeText(context, "High score will be added when you're back online.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Save high score to Firebase
    private fun saveHighScoreToFirebase(userId: String, username: String, highScore: Int?, context: Context) {
        val highScoreData = hashMapOf("userId" to userId, "username" to username, "highScore" to highScore)
        val db = FirebaseFirestore.getInstance()
        val leaderboardRef = db.collection("Leaderboard").document(userId)

        leaderboardRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val existingScore = document.getLong("highScore")?.toInt() ?: 0
                if (existingScore < highScore!!) {
                    leaderboardRef.set(highScoreData) // Update if new score is higher
                    Toast.makeText(context, "High score saved!", Toast.LENGTH_SHORT).show()
                }
            } else {
                leaderboardRef.set(highScoreData) // Add new record if it doesn't exist
                Toast.makeText(context, "High score saved!", Toast.LENGTH_SHORT).show()
            }
            pendingHighScore = null // Clear pending score after syncing
        }.addOnFailureListener { e ->
            e.printStackTrace()
            // Handle any errors in saving to Firestore
            Toast.makeText(context, "Failed to save high score.", Toast.LENGTH_SHORT).show()
        }
    }

    // Load high score from SharedPreferences
    private fun getHighScoreFromPreferences(context: Context): Int {
        val sharedPrefs = context.getSharedPreferences("SnakeGamePrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getInt("high_score", 0)
    }

    // Save high score to SharedPreferences
    private fun saveHighScoreToPreferences(context: Context, score: Int) {
        val sharedPrefs = context.getSharedPreferences("SnakeGamePrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putInt("high_score", score).apply()
    }

    // Check if the device is online
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
