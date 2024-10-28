package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Google Sign-In configuration
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Get references to views
        val googleSignInButton = findViewById<Button>(R.id.googleSignInButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // Normal registration logic
        registerButton.setOnClickListener {
            registerUser()
        }

        // Google Sign-In logic
        googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun registerUser() {
        // Your existing registration logic here
    }

    // Handle Google Sign-In result
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleGoogleSignInResult(task)
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(Exception::class.java)
            account?.let {
                firebaseAuthWithGoogle(it)
            }
        } catch (e: Exception) {
            Log.e("RegistrationActivity", "Google sign-in failed", e)
            Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val userMap = hashMapOf(
                        "name" to account.displayName,
                        "email" to account.email
                    )

                    userId?.let {
                        db.collection("users").document(it).set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Google sign-in successful", Toast.LENGTH_SHORT).show()
                                // Navigate to HomeActivity
                                val intent = Intent(this, Home::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Google sign-in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
