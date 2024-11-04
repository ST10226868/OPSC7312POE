package com.example.opsc7312poe.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.opsc7312poe.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if notifications are enabled
        if (!areNotificationsEnabled()) {
            Log.d("MessagingService", "Notifications are disabled by user")
            return
        }

        // Handle the received FCM message
        remoteMessage.notification?.let {
            showNotification(it.title ?: "New Message", it.body ?: "You have a new message!")
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "messages_channel"

        // Create the NotificationChannel if Android version is Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Messages",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for message notifications"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)  // Ensure logo resource is available
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Show the notification if permission is granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(1001, notification)
        }
    }

    private fun areNotificationsEnabled(): Boolean {
        // Retrieve notification preference from SharedPreferences
        val sharedPref = getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("notifications_enabled", false)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "New token: $token")
        // Optionally, send this token to your server to manage targeted messages
    }
}
