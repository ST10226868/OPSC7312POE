package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Leaderboard : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var leaderboardTable: TableLayout
    private lateinit var currentUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        // Initialize views
        backBtn = findViewById(R.id.BackButton)
        leaderboardTable = findViewById(R.id.leaderboardTable)

        // Get current user ID
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Set up back button with your existing navigation
        backBtn.setOnClickListener {
            val intent = Intent(this, SnakeHomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Load leaderboard data
        loadLeaderboardData()
    }

    private fun loadLeaderboardData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Leaderboard")
            .get()
            .addOnSuccessListener { documents ->
                // Convert documents to list of LeaderboardEntry
                val entries = documents.mapNotNull { doc ->
                    try {
                        LeaderboardEntry(
                            userId = doc.id,
                            username = doc.getString("username") ?: "Unknown",
                            highScore = doc.getLong("highScore")?.toInt() ?: 0
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

                // Sort entries by high score (descending)
                val sortedEntries = entries.sortedByDescending { it.highScore }

                // Clear existing rows except header
                for (i in leaderboardTable.childCount - 1 downTo 1) {
                    leaderboardTable.removeViewAt(i)
                }

                // Display top 10 entries
                sortedEntries.take(10).forEachIndexed { index, entry ->
                    addLeaderboardRow(index + 1, entry)
                }

                // Find and display current user's position
                val currentUserPosition = sortedEntries.indexOfFirst { it.userId == currentUserId }
                if (currentUserPosition >= 0 && currentUserPosition >= 10) {
                    // Add separator row
                    addSeparatorRow()
                    // Add current user's row
                    addLeaderboardRow(
                        currentUserPosition + 1,
                        sortedEntries[currentUserPosition],
                        isCurrentUser = true
                    )
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading leaderboard: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addLeaderboardRow(rank: Int, entry: LeaderboardEntry, isCurrentUser: Boolean = false) {
        val row = TableRow(this).apply {
            layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            if (isCurrentUser) {
                setBackgroundColor(getColor(R.color.ButtonLight))
            }
        }

        // Rank TextView
        val rankView = TextView(this).apply {
            text = rank.toString()
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f)
            setPadding(8, 8, 8, 8)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            textSize = 16f
            setTextColor(if (isCurrentUser) getColor(R.color.lightBackground) else getColor(R.color.ButtonLight))
        }

        // Username TextView
        val usernameView = TextView(this).apply {
            text = entry.username
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
            setPadding(8, 8, 8, 8)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            textSize = 16f
            setTextColor(if (isCurrentUser) getColor(R.color.lightBackground) else getColor(R.color.ButtonLight))
        }

        // Score TextView
        val scoreView = TextView(this).apply {
            text = entry.highScore.toString()
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f)
            setPadding(8, 8, 8, 8)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            textSize = 16f
            setTextColor(if (isCurrentUser) getColor(R.color.lightBackground) else getColor(R.color.ButtonLight))
        }

        row.addView(rankView)
        row.addView(usernameView)
        row.addView(scoreView)
        leaderboardTable.addView(row)
    }

    private fun addSeparatorRow() {
        val row = TableRow(this).apply {
            layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
        }

        val separatorView = TextView(this).apply {
            text = "..."
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f)
            setPadding(8, 8, 8, 8)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            textSize = 16f
            setTextColor(getColor(R.color.ButtonLight))
        }

        row.addView(separatorView)
        leaderboardTable.addView(row)
    }

    data class LeaderboardEntry(
        val userId: String,
        val username: String,
        val highScore: Int
    )
}