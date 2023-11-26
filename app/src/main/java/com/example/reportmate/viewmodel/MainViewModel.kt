package com.example.reportmate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reportmate.model.PushNotification
import com.example.reportmate.network.ApiManager
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel:ViewModel() {

    var resultMutableLiveData=MutableLiveData<Boolean>();
    var resultLiveData:LiveData<Boolean> =resultMutableLiveData;



    fun subscribeToNewTopic(topicInput: String, callback: TopicCallback) {
        FirebaseMessaging.getInstance().subscribeToTopic(topicInput)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSubscribed()
                    println("successfully subscribed to the topic")
                } else {
                    println("failed to subscribe to the topic")
                }
            }
            .addOnFailureListener {
                println("failed to subscribe to the topic : ${it.message}")
            }
    }
}

