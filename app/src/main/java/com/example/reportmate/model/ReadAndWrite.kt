package com.example.reportmate.model

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage

import java.time.LocalDateTime

class ReadAndWrite {
    private lateinit var database:DatabaseReference
    private lateinit var storage :StorageReference
    fun initializeDbRef() {
        // [START initialize_database_ref]
        database = Firebase.database.getReference("Incidents")
        // [END initialize_database_ref]
    }
    fun initialzeStorageRef(){
        storage = FirebaseStorage.getInstance().reference.child("incidents")
    }

    fun uploadImage(key:String,file: Uri, onComplete: (Boolean) -> Unit){
        initialzeStorageRef()
        storage=storage.child(key+"/"+System.currentTimeMillis().toString())
        var uploadTask = storage.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            onComplete(false)
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            onComplete(true)
        }
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

    fun writedata(
        title: String,
        latitude: Double?,
        longitude: Double?,
        description: String,
        happenedAt:String,
        generated_at: String,
        crime_type: String,
        officer_id: Int,
        phone: Int,
        status: String,
        updated_at: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        initializeDbRef()

        val incidentRecord = incident(
            title = title,
            latitude = latitude,
            longitude = longitude,
            description = description,
            happenedAt=happenedAt,
            generated_at = generated_at,
            crime_type = crime_type,
            officer_id = officer_id,
            phone = phone,
            status = status,
            updated_at = updated_at
        )

        // Get a new child location with a unique key
        val newIncidentRef = database.push()
        val newIncidentKey = newIncidentRef.key
        newIncidentRef.setValue(incidentRecord)

        // Set the value and add onCompleteListener
        newIncidentRef.setValue(incidentRecord)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Data was successfully inserted
                    onComplete(true, newIncidentKey)
                } else {
                    // Data insertion failed
                    onComplete(false,null)
                }
            }
    }

}