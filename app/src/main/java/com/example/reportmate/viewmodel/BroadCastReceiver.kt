package com.example.reportmate.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BroadCastReceiver {
    interface LocationCallback {
        fun onLocationUpdate(latitude: Double, longitude: Double)
    }

    private var locationCallback: LocationCallback? = null

    fun setLocationCallback(callback: LocationCallback) {
        locationCallback = callback
    }

    val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationService.ACTION_LOCATION_UPDATE) {
                val latitude = intent.getDoubleExtra(LocationService.EXTRA_LATITUDE, 0.0)
                val longitude = intent.getDoubleExtra(LocationService.EXTRA_LONGITUDE, 0.0)
                // Notify the callback
                locationCallback?.onLocationUpdate(latitude, longitude)


            }
        }
    }
}