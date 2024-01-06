package com.example.reportmate.view

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider


import com.example.reportmate.R
import com.example.reportmate.viewmodel.MainViewModel
import com.example.reportmate.viewmodel.TopicCallback

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var educationalDialog: AlertDialog
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                showToast("Notification permission is required to show notifications")
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
            ),
            0
        )
        // Initialize mainViewModel
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Subscribe to topic incidents
        val topicOnStart = "incidents"
        mainViewModel.subscribeToNewTopic(topicOnStart, object : TopicCallback {
            override fun onSubscribed() {

            }
        })

        setContentView(R.layout.activity_main)

    }

    private fun requestNotificationPermission() {
        if (isNotificationPermissionGranted()) {
            // Permission already granted
        } else {
            // Request permission using the launcher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PERMISSION_GRANTED
                ) {
                    // FCM SDK (and your app) can post notifications.
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // Build the custom educational dialog
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Notification Permission")
                        .setMessage("Granting the notification permission enables features such as receiving important updates and alerts. Do you want to enable notifications?")
                        .setPositiveButton("OK") { _, _ ->
                            // User clicked OK, request notification permission
                            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        .setNegativeButton("No thanks") { _, _ ->
                            // User clicked No thanks, handle accordingly (e.g., continue without notifications)
                            showToast("You choose not to enable notifications.")
                        }

                    // Create and show the dialog
                    educationalDialog = builder.create()
                    educationalDialog.show()
                } else {
                    // Directly ask for the permission
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PERMISSION_GRANTED
    }



    private fun showToast(message: String) {
        // Show a toast message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}