package com.example.opsc7312poe

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var textView2: TextView
    private lateinit var UserDisplay: TextView
    private lateinit var APDS: TextView
    private lateinit var PROG: TextView
    private lateinit var OPSC: TextView
    private lateinit var XBCAD: TextView
    private lateinit var NWEG: TextView
    private lateinit var ITTP: TextView
    private lateinit var addModuleButton: Button

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize TextViews and Button
        textView2 = view.findViewById(R.id.UserDisplay)
        APDS = view.findViewById(R.id.APDS)
        PROG = view.findViewById(R.id.PROG)
        OPSC = view.findViewById(R.id.OPSC)
        XBCAD = view.findViewById(R.id.XBCAD)
        NWEG = view.findViewById(R.id.NWEG)
        ITTP = view.findViewById(R.id.ITTP)
        UserDisplay = view.findViewById(R.id.UserDisplay)
        addModuleButton = view.findViewById(R.id.addModuleButton)

        // Retrieve the username from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Guest") // Default to "Guest" if not found

        // Set the username in the UserDisplay TextView
        UserDisplay.text = "Welcome, $username"

        // Set click listeners for navigation
        APDS.setOnClickListener { navigateToModuleView("APDS") }
        PROG.setOnClickListener { navigateToModuleView("PROG") }
        NWEG.setOnClickListener { navigateToModuleView("NWEG") }
        ITTP.setOnClickListener { navigateToModuleView("ITTP") }

        // Show Toast on click for XBCAD
        XBCAD.setOnClickListener { showToast("XBCAD clicked") }

        // Set click listener for the Add Module button
        addModuleButton.setOnClickListener {
            showToast("Add Module button clicked")
            // Add your logic here to add a new module or navigate to a new screen for module addition
        }

        return view
    }

    // Function to navigate to ModuleView with module name
    private fun navigateToModuleView(moduleName: String) {
        val intent = Intent(requireContext(), ModuleView::class.java).apply {
            putExtra("MODULE_NAME", moduleName)
        }
        startActivity(intent)
    }

    // Helper function to show toast
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
