package com.example.opsc7312poe

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class NotificationFragment : Fragment() {

    private lateinit var notificationsTextView: TextView
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var messageListener: ListenerRegistration

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

                        // Trigger notification for each new message
                        showNotification(requireContext(), sender ?: "Unknown", messageContent ?: "")
                    }
                    notificationsTextView.text = messages.toString()
                } else {
                    notificationsTextView.text = "No new messages."
                }
            }
    }

    private fun showNotification(context: Context, sender: String, message: String) {
        val channelId = "messages_channel"
        val notificationId = System.currentTimeMillis().toInt() // Unique ID for each notification

        // Check if permission is granted
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("New message from $sender")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } else {
            Toast.makeText(context, "Notification permission is not granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        messageListener.remove() // Stop listening for messages
    }
}
