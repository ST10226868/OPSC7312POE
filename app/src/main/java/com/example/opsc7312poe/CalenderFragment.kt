package com.example.opsc7312poe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CalenderFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var descriptionEditText: EditText

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

        // Initialize the DatePicker, TimePicker, and EditText
        datePicker = view.findViewById(R.id.datePicker)
        timePicker = view.findViewById(R.id.timePicker)
        descriptionEditText = view.findViewById(R.id.edit_text_description)

        // Set 24-hour view for TimePicker
        timePicker.setIs24HourView(true)

        // Get the current date and time
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // Set the current date in DatePicker
        datePicker.updateDate(currentYear, currentMonth, currentDay)

        // Set the current time in TimePicker
        timePicker.hour = currentHour
        timePicker.minute = currentMinute

        return view
    }

    // Function to get the selected date and time as a string
    fun getSelectedDateTime(): String {
        val day = datePicker.dayOfMonth
        val month = datePicker.month + 1  // Month is 0-indexed
        val year = datePicker.year

        val hour = timePicker.hour
        val minute = timePicker.minute

        return String.format("%02d/%02d/%d %02d:%02d", day, month, year, hour, minute)
    }

    // Function to get the description text entered by the user
    fun getDescription(): String {
        return descriptionEditText.text.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalenderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
