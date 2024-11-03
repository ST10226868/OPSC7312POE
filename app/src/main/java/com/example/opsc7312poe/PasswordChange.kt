package com.example.opsc7312poe
//
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


class PasswordChange : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmNewPasswordEditText: EditText
    private lateinit var saveBtn: Button
    private lateinit var backBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_change)

        auth = FirebaseAuth.getInstance()

        currentPasswordEditText = findViewById(R.id.currentPassword)
        newPasswordEditText = findViewById(R.id.newPassword)
        confirmNewPasswordEditText = findViewById(R.id.confirmPassword)

        saveBtn = findViewById(R.id.saveBtn)
        backBtn = findViewById(R.id.backBtn)

        saveBtn.setOnClickListener {
            changePassword()
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun changePassword() {
        val currentPassword = currentPasswordEditText.text.toString().trim()
        val newPassword = newPasswordEditText.text.toString().trim()
        val confirmNewPassword = confirmNewPasswordEditText.text.toString().trim()

        // Input Validation
        if (currentPassword.isBlank()) {
            currentPasswordEditText.error = "Current password can't be blank"
            currentPasswordEditText.requestFocus()
            return
        }
        if (newPassword.isBlank()) {
            newPasswordEditText.error = "New password can't be blank"
            newPasswordEditText.requestFocus()
            return
        }
        if (confirmNewPassword.isBlank()) {
            confirmNewPasswordEditText.error = "Confirm password can't be blank"
            confirmNewPasswordEditText.requestFocus()
            return
        }
        if (newPassword != confirmNewPassword) {
            confirmNewPasswordEditText.error = "New passwords do not match"
            confirmNewPasswordEditText.requestFocus()
            return
        }

        val user = auth.currentUser
        if (user != null) {
            // Reauthenticate the user with the current password
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential)
                .addOnSuccessListener {
                    // If reauthentication succeeds, update the password
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()

                        }
                        .addOnFailureListener { e ->
                            newPasswordEditText.error = "Failed to update password: ${e.message}"
                            newPasswordEditText.requestFocus()
                        }
                }
                .addOnFailureListener { e ->
                    currentPasswordEditText.error = "Current password is incorrect: ${e.message}"
                    currentPasswordEditText.requestFocus()
                }
        }
    }
}