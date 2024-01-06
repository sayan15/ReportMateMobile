package com.example.reportmate.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.reportmate.model.NotificationData
import com.example.reportmate.model.PushNotification
import com.example.reportmate.model.ReadAndWrite
import com.example.reportmate.network.ApiManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class finalViewModel:ViewModel() {
    var resultMutableLiveData= MutableLiveData<Boolean>();
    var resultLiveData: LiveData<Boolean> =resultMutableLiveData;
    // ViewModel
    private val apiManager = ApiManager()

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertData(
        title: String,
        latitude: Double?,
        longitude: Double?,
        description: String,
        happenedAt:String,
        officerId: Int,
        status: String,
        crimeType: String,
        phone: Long,
        updatedAt: String,
        onComplete: (Boolean,String?) -> Unit
    ) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val readAndWrite = ReadAndWrite()

        readAndWrite.writedata(
            title = title,
            latitude = latitude,
            longitude = longitude,
            description = description,
            happenedAt=happenedAt,
            generated_at = LocalDateTime.now().format(formatter),
            crime_type = crimeType,
            officer_id = officerId,
            phone = phone,
            status = status,
            updated_at = updatedAt
        ){ isSuccess ,newIncidentKey->
            if (isSuccess) {
                // Data was successfully inserted
                onComplete(true,newIncidentKey)
                PushNotification(
                    NotificationData(crimeType, (" happened at $title"), LatLng(latitude!!,longitude!!)),
                    "/topics/incidents"
                ).also {
                    //Todo explain you can do foreach topic send notification

                    sendNewMessage(it)
                }
            } else {
                // Data insertion failed
                onComplete(false,null)
            }
        }
    }

    fun sendNewMessage(notification: PushNotification) {
        viewModelScope.launch(Dispatchers.IO) {
            apiManager.postNotification(notification)
        }
    }


}