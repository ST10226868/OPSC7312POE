package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class NewNoteView : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note_view)

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance()

        // Find view components
        val saveNoteButton = findViewById<Button>(R.id.SaveNote)
        val backButton = findViewById<Button>(R.id.Back)
        val moduleTitleInput = findViewById<EditText>(R.id.Note_Title)
        val noteDescriptionInput = findViewById<EditText>(R.id.editTextTextMultiLine)

        // Save Note button logic
        saveNoteButton.setOnClickListener {
            val moduleTitle = moduleTitleInput.text.toString()
            val noteDescription = noteDescriptionInput.text.toString()

            // Create note object to store in Firestore
            val note = hashMapOf(
                "moduleTitle" to moduleTitle,
                "noteDescription" to noteDescription
            )

            // Save note to Firebase Firestore
            firestore.collection("notes")
                .add(note)
                .addOnSuccessListener {
                    // On success, navigate back to NotesView
                    val intent = Intent(this, NotesView::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    // Handle error (if any)
                    e.printStackTrace()
                }
        }

        // Back button logic
        backButton.setOnClickListener {
            val intent = Intent(this, NotesView::class.java)
            startActivity(intent)
        }
    }
}
