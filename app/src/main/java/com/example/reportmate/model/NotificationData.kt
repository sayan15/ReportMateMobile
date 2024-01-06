package com.example.reportmate.model

import com.google.android.gms.maps.model.LatLng

data class NotificationData(
    val title: String,
    val message: String,
    val latlng:LatLng
)
