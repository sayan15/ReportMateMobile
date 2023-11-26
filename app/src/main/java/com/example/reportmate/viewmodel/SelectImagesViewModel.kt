package com.example.reportmate.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.reportmate.model.ReadAndWrite

class SelectImagesViewModel:ViewModel() {

    private val model = ReadAndWrite() // Assuming YourModel is the name of your model class
    // Inside your ViewModel
    fun uploadImagesToStorage(key: String,imageUris: List<Uri>,onComplete: (Boolean) -> Unit) {
        for (imageUri in imageUris) {
            uploadImage(key,imageUri){ isSuccess ->
                if (isSuccess) {
                    // Upload successful
                    onComplete(true)
                } else {
                    // Upload failed
                    onComplete(false)
                }
            }
        }
    }
    fun uploadImage(key: String, file: Uri,onComplete: (Boolean) -> Unit) {
        model.uploadImage(key, file){ isSuccess ->
            if (isSuccess) {
                // Upload successful
                onComplete(true)
            } else {
                // Upload failed
                onComplete(false)
            }
        }
    }
}