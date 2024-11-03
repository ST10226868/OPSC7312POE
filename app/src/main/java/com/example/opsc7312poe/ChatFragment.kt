package com.example.opsc7312poe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Timestamp

class ChatFragment : Fragment() {

    private lateinit var addNewChatButton: Button
    private lateinit var recipientEmailEditText: EditText
    private lateinit var messageEditText: EditText
    private lateinit var sendMessageButton: Button
    private lateinit var chatStatusTextView: TextView

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        addNewChatButton = view.findViewById(R.id.addNewChatButton)
        recipientEmailEditText = view.findViewById(R.id.recipientEmailEditText)
        messageEditText = view.findViewById(R.id.messageEditText)
        sendMessageButton = view.findViewById(R.id.sendMessageButton)
        chatStatusTextView = view.findViewById(R.id.chatStatusTextView)

        addNewChatButton.setOnClickListener {
            recipientEmailEditText.visibility = View.VISIBLE
            sendMessageButton.visibility = View.VISIBLE
            chatStatusTextView.visibility = View.GONE
        }

        sendMessageButton.setOnClickListener {
            val recipientEmail = recipientEmailEditText.text.toString()
            validateEmail(recipientEmail)
        }

        return view
    }

    private fun validateEmail(email: String) {
        val isValidEmail = email.isNotEmpty() && email.contains("@")

        if (isValidEmail) {
            chatStatusTextView.text = "Valid email. Please enter your message."
            chatStatusTextView.visibility = View.VISIBLE
            messageEditText.visibility = View.VISIBLE
            sendMessageButton.visibility = View.VISIBLE

            sendMessageButton.setOnClickListener {
                sendMessage(email, messageEditText.text.toString())
            }
        } else {
            chatStatusTextView.text = "Invalid email."
            chatStatusTextView.visibility = View.VISIBLE
            messageEditText.visibility = View.GONE
            sendMessageButton.visibility = View.GONE
        }
    }

    private fun sendMessage(recipientEmail: String, message: String) {
        val senderEmail = auth.currentUser?.email ?: return

        val messageData = hashMapOf(
            "sender" to senderEmail,
            "recipient" to recipientEmail,
            "content" to message,
            "timestamp" to Timestamp.now()
        )

        firestore.collection("messages")
            .add(messageData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    chatStatusTextView.text = "Message sent!"
                    // Optional: Notify recipient (handled in NotificationFragment)
                } else {
                    chatStatusTextView.text = "Failed to send message."
                }
                chatStatusTextView.visibility = View.VISIBLE
                messageEditText.setText("")
            }
    }
}
