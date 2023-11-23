package com.example.reportmate.model

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.*
import java.time.LocalDateTime

class ReadAndWrite {
    private lateinit var database:DatabaseReference

    fun initializeDbRef() {
        // [START initialize_database_ref]
        database = Firebase.database.getReference("Incidents")
        // [END initialize_database_ref]
    }

    fun getData(){
        initializeDbRef();
        // Read from the database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.value
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun writedata(description: String,
                  generated_at: String,
                  latitude: Double, longitude: Double,
                  officer_id: Int,
                  status: String,
                  title: String,
                  updated_at: String
    ){
        initializeDbRef();
        val incident_record=incident(description,generated_at, latitude, longitude, officer_id, status, title, updated_at)
        // Get a new child location with a unique key
        val newIncidentRef = database.push()
        newIncidentRef.setValue(incident_record);
    }
}