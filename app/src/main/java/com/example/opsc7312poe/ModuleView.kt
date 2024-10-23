package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ModuleView : AppCompatActivity() {
    private lateinit var moduleTitleTextView: TextView // Use a property instead of a local variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_module_view)

        // Initialize views
        val backButton: Button = findViewById(R.id.Back)
        val notesButton: Button = findViewById(R.id.Notes)
        moduleTitleTextView = findViewById(R.id.ModuleTitle) // Correctly initialize the TextView

        // Retrieve the module name from the Intent
        val moduleName = intent.getStringExtra("MODULE_NAME") ?: "Module"
        moduleTitleTextView.text = moduleName // Set the module title in the TextView

        // Set up the Notes button click listener
        notesButton.setOnClickListener {
            val intent = Intent(this@ModuleView, NotesView::class.java)
            startActivity(intent)
        }

        // Back button click listener
        backButton.setOnClickListener {
            finish()
        }

        // Handle window insets for edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ModuleView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
