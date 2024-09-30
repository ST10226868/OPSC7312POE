package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Define the default profile picture URL as a constant
    private val DEFAULT_PROFILE_PIC_URL = "https://firebasestorage.googleapis.com/v0/b/opsc7312poe-b2aea.appspot.com/o/no_pfp.png?alt=media&token=25276e28-5867-4c6e-b792-5289f2cf5171"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val surnameEditText = findViewById<EditText>(R.id.surnameEditText)
        val studentNumberEditText = findViewById<EditText>(R.id.studentNumberEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val surname = surnameEditText.text.toString().trim()
            val studentNumber = studentNumberEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (name.isEmpty() || surname.isEmpty() || studentNumber.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(name, surname, studentNumber, email, password)
        }
    }

    private fun registerUser(name: String, surname: String, studentNumber: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        createUserDocument(userId, name, surname, studentNumber, email)
                    } else {
                        Toast.makeText(this, "Failed to get user ID", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createUserDocument(userId: String, name: String, surname: String, studentNumber: String, email: String) {
        val userMap = hashMapOf(
            "name" to name,
            "surname" to surname,
            "studentNumber" to studentNumber,
            "email" to email,
            "profilePicUrl" to DEFAULT_PROFILE_PIC_URL
        )

        db.collection("users").document(userId).set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                // Navigate to LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}