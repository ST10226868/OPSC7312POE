package com.example.opsc7312poe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment

class CalendarFragment : Fragment() { // Corrected the class name from CalenderFragment to CalendarFragment

    // Declare views
    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var descriptionEditText: EditText
    private lateinit var savedEntriesContainer: LinearLayout
    private lateinit var saveButton: Button

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calender, container, false)

        // Initialize views
        datePicker = view.findViewById(R.id.datePicker)
        timePicker = view.findViewById(R.id.timePicker)
        descriptionEditText = view.findViewById(R.id.edit_text_description)
        savedEntriesContainer = view.findViewById(R.id.saved_entries_container)

        // Create the save button programmatically (or add this in the XML layout if preferred)
        saveButton = Button(context).apply {
            text = "Save"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 0) // Adding margin to the button for better spacing
            }
        }

        // Add the save button to the layout
        (view as LinearLayout).addView(saveButton)

        // Set TimePicker to 24-hour mode
        timePicker.setIs24HourView(true)

        // Handle save button click
        saveButton.setOnClickListener {
            saveEntry()
        }

        return view
    }

    // Function to save the entry
    private fun saveEntry() {
        // Get date from DatePicker
        val day = datePicker.dayOfMonth
        val month = datePicker.month + 1 // DatePicker month is zero-based
        val year = datePicker.year

        // Get time from TimePicker
        val hour = timePicker.hour
        val minute = timePicker.minute

        // Get description from EditText
        val description = descriptionEditText.text.toString()

        // Create a new TextView for the saved entry
        val entryView = TextView(context).apply {
            text = "Date: $day/$month/$year, Time: ${String.format("%02d", hour)}:${String.format("%02d", minute)}, Description: $description"
            textSize = 16f
            setPadding(8, 8, 8, 8)
        }

        // Add the new entry to the saved entries container
        savedEntriesContainer.addView(entryView)

        // Clear the description field after saving
        descriptionEditText.text.clear()
    }

    companion object {
        private const val ARG_PARAM1 = "param1" // Added constants for argument keys
        private const val ARG_PARAM2 = "param2"

        // Use this factory method to create a new instance of this fragment using the provided parameters.
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply { // Corrected to CalendarFragment
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
