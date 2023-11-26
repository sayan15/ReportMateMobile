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


}