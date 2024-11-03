package com.example.opsc7312poe
//
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class AccountSettings : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null
    private var isProfilePictureChanged = false

    private lateinit var pfpImage: ImageView
    private lateinit var studentNumber: EditText
    private lateinit var userName: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveBtn: Button
    private lateinit var backBtn: Button

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        // Initialize views here after setContentView
        pfpImage = findViewById(R.id.pfpImage)
        studentNumber = findViewById(R.id.studentNumber)
        userName = findViewById(R.id.userName)
        emailEditText = findViewById(R.id.emailEditText)
        saveBtn = findViewById(R.id.saveBtn)
        backBtn = findViewById(R.id.backBtn)

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Load user data
        loadUserData()

        // Set up profile picture click listener
        pfpImage.setOnClickListener {
            openImageChooser()
        }

        // Set up save button click listener
        saveBtn.setOnClickListener {
            saveUserData()
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        userId?.let { uid ->
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        studentNumber.setText(document.getString("studentNumber"))
                        userName.setText(document.getString("name"))
                        emailEditText.setText(document.getString("email"))

                        // Load profile picture
                        val profilePicUrl = document.getString("profilePicUrl")
                        if (!profilePicUrl.isNullOrEmpty()) {
                            Picasso.get().load(profilePicUrl).into(pfpImage)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error loading user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            pfpImage.setImageURI(selectedImageUri)
            isProfilePictureChanged = true
        }
    }

    private fun saveUserData() {
        val userId = auth.currentUser?.uid
        userId?.let { uid ->
            val updatedData = hashMapOf<String, Any>()

            // Check for changes in editable fields, skip null or empty inputs
            userName.text?.let { newName ->
                if (newName.isNotEmpty() && newName.toString() != (userName.tag?.toString() ?: "")) {
                    updatedData["name"] = newName.toString()
                }
            }

            // Proceed only if there are changes or the profile picture is being updated
            if (updatedData.isNotEmpty() || isProfilePictureChanged) {
                if (isProfilePictureChanged) {
                    uploadProfilePicture(uid) { profilePicUrl ->
                        updatedData["profilePicUrl"] = profilePicUrl
                        updateFirestore(uid, updatedData)
                    }
                } else {
                    updateFirestore(uid, updatedData)
                }
            } else {
                Toast.makeText(this, "No changes were made", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadProfilePicture(userId: String, onSuccess: (String) -> Unit) {
        // Ensure this path matches the rules
        val ref = storage.reference.child("profile_pictures/$userId.jpg")
        selectedImageUri?.let { uri ->
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { downloadUri ->
                        onSuccess(downloadUri.toString())
                        Toast.makeText(this, "Profile Picture updated successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to upload profile picture: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } ?: run {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateFirestore(userId: String, data: Map<String, Any>) {
        db.collection("users").document(userId).update(data)
            .addOnSuccessListener {
                Toast.makeText(this, "User data updated successfully", Toast.LENGTH_SHORT).show()
                // Update tags to reflect new values
                userName.tag = userName.text.toString()
                isProfilePictureChanged = false
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update user data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}