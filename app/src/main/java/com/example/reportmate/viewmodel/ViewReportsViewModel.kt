package com.example.reportmate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reportmate.model.CrimeData
import com.example.reportmate.model.ReadAndWrite

class ViewReportsViewModel : ViewModel() {
    var resultMutableLiveData= MutableLiveData<CrimeData>();
    var resultLiveData: LiveData<CrimeData> =resultMutableLiveData;

    fun getReports(phoneNumber: Long,onComplete: (Boolean, List<CrimeData>) -> Unit){

        val readAndWrite=ReadAndWrite()

        readAndWrite.getDataPhone(phoneNumber){isSuccess,crimeData->
            if (isSuccess){
                onComplete(true,crimeData)

            }

        }
    }
}