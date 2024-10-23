package com.example.opsc7312poe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val notesList: List<Note>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    // Define the ViewHolder
    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moduleTitleTextView: TextView = view.findViewById(R.id.moduleTitleTextView)
        val noteDescriptionTextView: TextView = view.findViewById(R.id.noteDescriptionTextView)
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        // Inflate the item_note layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        // Get the note at the current position
        val note = notesList[position]

        // Set the text for the module title and note description
        holder.moduleTitleTextView.text = "Module: ${note.moduleTitle}"
        holder.noteDescriptionTextView.text = "Description: ${note.noteDescription}"
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return notesList.size
    }
}
