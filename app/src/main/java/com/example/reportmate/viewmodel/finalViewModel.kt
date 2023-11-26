package com.example.reportmate.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reportmate.model.ReadAndWrite
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class finalViewModel:ViewModel() {
    var resultMutableLiveData= MutableLiveData<Boolean>();
    var resultLiveData: LiveData<Boolean> =resultMutableLiveData;

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
        phone: Int,
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
            } else {
                // Data insertion failed
                onComplete(false,null)
            }
        }
    }


}