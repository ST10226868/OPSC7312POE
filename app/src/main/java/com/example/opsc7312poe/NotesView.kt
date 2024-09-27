package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class NotesView : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private var notesList: MutableList<Note> = mutableListOf()
    private lateinit var moduleTitleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notes_view)

        // Initialize Firestore instance
        firestore = FirebaseFirestore.getInstance()

        // Set up RecyclerView
        recyclerViewNotes = findViewById(R.id.AssignmentsInfo)
        recyclerViewNotes.layoutManager = LinearLayoutManager(this)

        // Buttons
        val newNoteButton = findViewById<Button>(R.id.AddNewNote)
        val backButton = findViewById<Button>(R.id.Back)
        moduleTitleTextView = findViewById(R.id.ModuleTitle)

        // Retrieve the module name from the Intent
        val moduleName = intent.getStringExtra("MODULE_NAME") ?: "Module"
        moduleTitleTextView.text = moduleName // Set the module title in the TextView

        // Button listeners
        newNoteButton.setOnClickListener {
            val intent = Intent(this, NewNoteView::class.java)
            // Pass the module name to NewNoteView if needed
            intent.putExtra("MODULE_NAME", moduleName)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            // Return to the ModuleView and pass the module name back
            val intent = Intent(this, ModuleView::class.java)
            intent.putExtra("MODULE_NAME", moduleName)
            startActivity(intent)
        }

        // Load notes from Firestore
        loadNotes()

        // Handle system bars (adjust layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.NotesView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadNotes() {
        firestore.collection("notes")
            .get()
            .addOnSuccessListener { result ->
                notesList.clear() // Clear the list before adding new data
                for (document in result) {
                    val moduleTitle = document.getString("moduleTitle") ?: ""
                    val noteDescription = document.getString("noteDescription") ?: ""

                    val note = Note(moduleTitle, noteDescription)
                    notesList.add(note)
                }

                // Set the adapter with the updated list
                notesAdapter = NotesAdapter(notesList)
                recyclerViewNotes.adapter = notesAdapter
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }
    }
}
