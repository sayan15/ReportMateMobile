package com.example.reportmate.viewmodel

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.reportmate.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class MyFirebaseMessagingService:FirebaseMessagingService(),BroadCastReceiver.LocationCallback {
    private val BroadCastReceiverInstance = BroadCastReceiver()
    private var isReceiverRegistered = false
    private var  latLng: LatLng ?=null
    private var latLngString:String?=null
    private var message:RemoteMessage?=null
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        println("This is the token: $newToken")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        println("message received")
        // Start the LocationService
        latLngString = message.data["latlng"]
        this.message=message
        startLocationService()
        //showNotification(message)
        // Validate location
        checkLocationWithReceiver()


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

    private fun startLocationService() {
        val startServiceIntent = Intent(applicationContext, LocationService::class.java)
        startServiceIntent.action = LocationService.ACTION_START
        startService(startServiceIntent)
        Log.d("LocationService","Location service has been started")
    }

    private fun checkLocationWithReceiver() {
        BroadCastReceiverInstance.setLocationCallback(this)

        // Register the receiver in onCreateView or onStart if not registered
        if (!isReceiverRegistered) {
            val filter = IntentFilter(LocationService.ACTION_LOCATION_UPDATE)
            registerReceiver(BroadCastReceiverInstance.locationReceiver, filter)
            isReceiverRegistered = true
            Log.d("LocationReceiverRegistered","Location receiver registered")
        }
    }

    override fun onLocationUpdate(latitude: Double, longitude: Double) {
        Log.d("NotificationLocationUpdate", "Latitude: $latitude, Longitude: $longitude")
        latLng= LatLng(latitude,longitude)

        if (latLngString != null && latLng!=null) {

            // Parse the JSON string
            val json = JSONObject( latLngString)

            // Get latitude and longitude values
            val latitude = json.getDouble("latitude")
            val longitude = json.getDouble("longitude")


            val R = 6371 // Earth radius in kilometers

            val dLat = Math.toRadians((latLng!!.latitude) - latitude)
            val dLon = Math.toRadians((latLng!!.longitude) - longitude)

            val a = sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians((latLng!!.latitude))) *
                    cos(Math.toRadians(latitude)) * sin(dLon / 2) * sin(dLon / 2)

            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            val distance= R * c

            if (distance <= 5.0) {
                // The distance is within 5 km
                Log.d("DistanceCheck", "Within 5 km")
                showNotification(message!!)
            } else {
                // The distance is greater than 5 km
                Log.d("DistanceCheck", "Greater than 5 km")
            }


        } else {
            Log.e("Error", "Invalid or null latlng format")
        }
    }

    // Remember to unregister the receiver when it's no longer needed (e.g., in onDestroy)
    override fun onDestroy() {
        if (isReceiverRegistered) {
            unregisterReceiver(BroadCastReceiverInstance.locationReceiver)
            isReceiverRegistered = false
        }
        super.onDestroy()
    }



}