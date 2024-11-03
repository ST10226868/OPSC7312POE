package com.example.opsc7312poe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class CalenderFragment : Fragment() {

    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var descriptionEditText: EditText
    private lateinit var savedEntriesContainer: LinearLayout

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

        // Configure timePicker to 24-hour view
        timePicker.setIs24HourView(true)

        // Set a listener for the date and time pickers
        view.findViewById<Button>(R.id.save_button)?.setOnClickListener {
            saveEntry()
        }

        return view
    }

    private fun saveEntry() {
        // Retrieve date from DatePicker
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        // Retrieve time from TimePicker
        val hour = timePicker.hour
        val minute = timePicker.minute
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        // Format date and time
        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateTime = dateTimeFormat.format(calendar.time)

        // Retrieve description
        val description = descriptionEditText.text.toString()

        // Create a new TextView for the saved entry
        val entryTextView = TextView(requireContext()).apply {
            text = "$dateTime - $description"
            textSize = 16f
            setPadding(8, 8, 8, 8)
        }

        // Add the TextView to the container
        savedEntriesContainer.addView(entryTextView)

        // Clear description field
        descriptionEditText.text.clear()
    }
}
