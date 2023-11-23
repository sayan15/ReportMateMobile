package com.example.reportmate.viewmodel

import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reportmate.R
import com.example.reportmate.databinding.FragmentSubmitReportBinding
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

class SubmitReportViewModel:ViewModel() {
    var resultMutableLiveData= MutableLiveData<String>();
    var resultLiveData: LiveData<String> =resultMutableLiveData;


    // Function to get place title from LatLng


    fun getPlaceTitleFromLatLng(context: Context,latLng:LatLng){
        val geocoder = Geocoder(context, Locale.getDefault())
        var addressText = ""

        try {
            val addresses: List<Address>? = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )

            if (addresses != null && addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                addressText = address.getAddressLine(0) // You can modify this to get more address details if needed
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error getting place title: ${e.message}")
        }

        resultMutableLiveData.value=addressText
    }



}