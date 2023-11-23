package com.example.reportmate.viewmodel

import android.location.Location
import kotlinx.coroutines.flow.Flow


interface UserLocation {
    fun LocationUpdates(interval:Long): Flow<Location>

    class locationException(message:String):Exception()
}