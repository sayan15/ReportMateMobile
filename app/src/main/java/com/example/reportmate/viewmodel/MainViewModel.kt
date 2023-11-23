package com.example.reportmate.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reportmate.model.ReadAndWrite
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainViewModel:ViewModel() {
    var resultMutableLiveData=MutableLiveData<Boolean>();
    var resultLiveData:LiveData<Boolean> =resultMutableLiveData;
    fun computation(var1:Int,var2:Int){
        val add=var1+var2
        resultMutableLiveData.value=add <=100
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun test(){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val readAndWrite = ReadAndWrite()
        readAndWrite.writedata( description = "Becket's Park is a lovely green space by the river.",
        generated_at = LocalDateTime.now().format(formatter), // Replace with the actual date and time
        latitude = 52.241,
        longitude = -0.8922,
        officer_id = 3,
        status = "no",
        title = "Becket's Park",
        updated_at = LocalDateTime.parse("2023-11-10T13:28:56").format(formatter)) // Replace with the actual date and time;
        readAndWrite.getData();
    }
}