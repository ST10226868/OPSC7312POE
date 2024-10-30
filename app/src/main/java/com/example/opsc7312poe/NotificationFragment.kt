package com.example.opsc7312poe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration

class NotificationFragment : Fragment() {

    private lateinit var notificationsTextView: TextView
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var messageListener: ListenerRegistration

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        notificationsTextView = view.findViewById(R.id.notificationsTextView)

        listenForMessages()

        return view
    }

    private fun listenForMessages() {
        val currentUserEmail = auth.currentUser?.email ?: return

        messageListener = firestore.collection("messages")
            .whereEqualTo("recipient", currentUserEmail)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    notificationsTextView.text = "Error fetching messages."
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val messages = StringBuilder("Notifications:\n")
                    for (doc in snapshot.documents) {
                        val messageContent = doc.getString("content")
                        val sender = doc.getString("sender")
                        messages.append("New message from $sender: $messageContent\n")
                    }
                    notificationsTextView.text = messages.toString()
                } else {
                    notificationsTextView.text = "No new messages."
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        messageListener.remove() // Stop listening for messages
    }
}
