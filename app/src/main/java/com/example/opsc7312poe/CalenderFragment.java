package com.example.opsc7312poe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CalenderFragment extends Fragment {

    // Declare views
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText descriptionEditText;
    private LinearLayout savedEntriesContainer;
    private Button saveButton;

    private String param1;
    private String param2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param1 = getArguments().getString(ARG_PARAM1);
            param2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calender, container, false);

        // Initialize views
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        descriptionEditText = view.findViewById(R.id.edit_text_description);
        savedEntriesContainer = view.findViewById(R.id.saved_entries_container);

        // Create the save button programmatically
        saveButton = new Button(getContext());
        saveButton.setText("Save");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 16, 0, 0); // Adding margin to the button for better spacing
        saveButton.setLayoutParams(layoutParams);

        // Add the save button to the layout
        ((LinearLayout) view).addView(saveButton);

        // Set TimePicker to 24-hour mode
        timePicker.setIs24HourView(true);

        // Handle save button click
        saveButton.setOnClickListener(v -> saveEntry());

        return view;
    }

    // Function to save the entry
    private void saveEntry() {
        // Get date from DatePicker
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // DatePicker month is zero-based
        int year = datePicker.getYear();

        // Get time from TimePicker
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        // Get description from EditText
        String description = descriptionEditText.getText().toString();

        // Create a new TextView for the saved entry
        TextView entryView = new TextView(getContext());
        entryView.setText("Date: " + day + "/" + month + "/" + year +
                ", Time: " + String.format("%02d", hour) + ":" + String.format("%02d", minute) +
                ", Description: " + description);
        entryView.setTextSize(16f);
        entryView.setPadding(8, 8, 8, 8);

        // Add the new entry to the saved entries container
        savedEntriesContainer.addView(entryView);

        // Clear the description field after saving
        descriptionEditText.setText("");
    }

    // Constants for argument keys
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Use this factory method to create a new instance of this fragment using the provided parameters.
    public static CalenderFragment newInstance(String param1, String param2) {
        CalenderFragment fragment = new CalenderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}
