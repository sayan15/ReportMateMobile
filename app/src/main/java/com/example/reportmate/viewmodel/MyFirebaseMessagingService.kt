package com.example.reportmate.viewmodel

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.reportmate.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService:FirebaseMessagingService() {
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        println("This is the token: $newToken")
    }

    override fun onMessageReceived(message: RemoteMessage) {

        showNotification(message)

    }


    private fun showNotification(message: RemoteMessage) {
        val notification = NotificationCompat.Builder(applicationContext, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .build()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }



}