package com.example.reportmate.view.fragments

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.reportmate.R
import com.example.reportmate.databinding.FragmentSubmitReportBinding
import com.example.reportmate.model.FinalData
import com.example.reportmate.model.latitudeLongitude
import com.example.reportmate.viewmodel.BroadCastReceiver
import com.example.reportmate.viewmodel.LocationService
import com.example.reportmate.viewmodel.SubmitReportViewModel
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class SubmitReportFragment : Fragment(),BroadCastReceiver.LocationCallback {
    private lateinit var  navController: NavController
    private lateinit var datePicker: DatePicker
    private var _binding: FragmentSubmitReportBinding? = null
    // This property is only valid between onCreateView
    private val binding get() = _binding!!
    private var isReceiverRegistered = false
    private lateinit var viewModel: SubmitReportViewModel
    private var  latLng: LatLng ?=null
    private var formattedDateTime:String=""
    private val BroadCastReceiverInstance = BroadCastReceiver()
    // Declare a public variable to store the placeTitle
    var storedPlaceTitle: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setting up the result listener

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // inflate the layout and bind to the _binding
        _binding = FragmentSubmitReportBinding.inflate(inflater, container, false)
        val view = binding.root
        val latitudeLongitude = arguments?.getParcelable<latitudeLongitude>("latitudeLongitude")
        // use the latitudeLongitude object
        if (latitudeLongitude != null) {
            // make checked radio btn
            binding.radioButtonOption2grp1.isChecked = true

            // Assign value address
            binding.selectTextView.text = "${latitudeLongitude.title}"
            storedPlaceTitle=latitudeLongitude.title
            latLng=latitudeLongitude.latlng
            binding.locationContainer.visibility=View.VISIBLE
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker=binding.datePicker
        //check when the option one is selected
        binding.radioButtonOption1grp2.setOnClickListener {
            toggleDatePickerVisibility(binding.radioButtonOption2grp2)
        }
        //check when the option two is selected
        binding.radioButtonOption2grp2.setOnClickListener {
            toggleDatePickerVisibility(binding.radioButtonOption2grp2)
        }
        //get current location
        binding.radioButtonOption1grp1.setOnClickListener {
            togglelocationContainerVisibility(false)

            val startServiceIntent = Intent(requireContext(), LocationService::class.java)
            startServiceIntent.action = LocationService.ACTION_START
            requireContext().startService(startServiceIntent)

            // Set the location callback
            BroadCastReceiverInstance.setLocationCallback(this)

            // Register the receiver in onCreateView or onStart if not registered
            if (!isReceiverRegistered) {
                val filter = IntentFilter(LocationService.ACTION_LOCATION_UPDATE)
                requireActivity().registerReceiver(BroadCastReceiverInstance.locationReceiver, filter)
                isReceiverRegistered = true
            }

        }
        //load container
        binding.radioButtonOption2grp1.setOnClickListener {
            togglelocationContainerVisibility(true)
            // Unregister the receiver in onDestroyView if registered
            if (isReceiverRegistered) {
                requireActivity().unregisterReceiver(BroadCastReceiverInstance.locationReceiver)
                isReceiverRegistered = false
            }
        }

        //navigate to map fragment
        binding.selectTextView.setOnClickListener {
            navController= Navigation.findNavController(view)
            navController.navigate(R.id.action_submitReportFragment_to_mapFragment)
        }

        //navigate to next page
        binding.nextButton.setOnClickListener {
            //get the date from date picker if user selected it
            if(binding.radioButtonOption2grp2.isChecked){
                // Get the selected date from the DatePicker
                val year = datePicker.year
                val month = datePicker.month + 1 // Month is 0-indexed
                val dayOfMonth = datePicker.dayOfMonth

                // Get the selected time from the TimePicker
                // Split the selectedTime string into hours and minutes
                val (hour, minute) = binding.timesetter.text.split(":").map { it.toInt() }

                // Now you have the selected date and time
                val selectedDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute)

                // Do something with the selectedDateTime
                // For example, you can convert it to a string in your desired format
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                formattedDateTime = selectedDateTime.format(formatter)

                Log.d("SelectedDateTime", formattedDateTime)
            }
            else{
                // Get the current date and time
                val currentDateTime = LocalDateTime.now()

                // Format the current date and time
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                formattedDateTime = currentDateTime.format(formatter)

                // Log the formatted date and time
                Log.d("SelectedDateTime", formattedDateTime)
            }

            //get the location

            Log.d("LiveDataUpdate", "Place Title: $storedPlaceTitle,$latLng")

            if((binding.radioButtonOption1grp1.isChecked or binding.radioButtonOption2grp1.isChecked ) and (binding.radioButtonOption1grp2.isChecked or binding.radioButtonOption2grp2.isChecked ) ){
                val finalData= FinalData(latLng,storedPlaceTitle.toString(),formattedDateTime)
                val bundle = Bundle()
                bundle.putParcelable("finalData", finalData)
                navController= Navigation.findNavController(view)
                navController.navigate(R.id.action_submitReportFragment_to_finalFragment,bundle)
            }
            else{
                Toast.makeText(requireContext(), "Please select options", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // Unregister the receiver in onDestroyView if registered
        if (isReceiverRegistered) {
            requireActivity().unregisterReceiver(BroadCastReceiverInstance.locationReceiver)
            isReceiverRegistered = false
        }
    }

    // Method to toggle the visibility of the DatePicker
    private fun toggleDatePickerVisibility(radioButtonOption: RadioButton) {
        if (radioButtonOption.isChecked) {

            datePicker.visibility = View.VISIBLE
            binding.timePicker.visibility=View.VISIBLE
            //set currnt time
            // Get the current date and time
            val currentDateTime = LocalDateTime.now()

            // Format the current date and time
            binding.timesetter.text=String.format("%02d:%02d", currentDateTime.hour, currentDateTime.minute)
            // Set an OnClickListener for the TextView
            binding.timesetter.setOnClickListener {
                showTimePickerDialog();
            }

        } else {
            datePicker.visibility = View.GONE
            binding.timePicker.visibility=View.GONE
        }
    }

    // Method to toggle the visibility of the locationContainer
    private fun togglelocationContainerVisibility(radioButtonOption:Boolean) {
        if (radioButtonOption) {
            binding.locationContainer.visibility=View.VISIBLE
        } else {
            binding.locationContainer.visibility=View.GONE
        }
    }
    //time picker pop up
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        var selectedTime=String.format("%02d:%02d", currentHour, currentMinute )
        val timePicker = TimePicker(requireActivity())
        timePicker.setIs24HourView(true) // Set true for 24-hour format
        timePicker.hour = currentHour
        timePicker.minute = currentMinute
        // Create a TimePickerDialog with a custom view
        val timePickerDialog = AlertDialog.Builder(requireActivity())
            .setTitle("Select Time")
            .setView(timePicker)
            .setPositiveButton("OK") { _, _ ->
                // Handle the selected time
                selectedTime = String.format("%02d:%02d", timePicker.hour, timePicker.minute)
                // Update the TextView immediately when the time is selected
                binding.timesetter.text = selectedTime
            }
            .setNegativeButton("Cancel", null)
            .create()

        // Show the TimePickerDialog
        timePickerDialog.show()

    }

    // Implement the onLocationUpdate function to handle the updated location values
    override fun onLocationUpdate(latitude: Double, longitude: Double) {
        // Perform actions based on the updated location values
        Log.d("LocationUpdate", "Latitude: $latitude, Longitude: $longitude")
        latLng =LatLng(latitude,longitude)
        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(SubmitReportViewModel::class.java)

        // Call the ViewModel function
        viewModel.getPlaceTitleFromLatLng(requireContext(), LatLng(latitude, longitude))

        // Log the live data value (assuming your LiveData is of type String, adjust accordingly)
        viewModel.resultLiveData.observe(viewLifecycleOwner) { placeTitle ->
            Log.d("LiveDataUpdate", "Place Title: $placeTitle")
            storedPlaceTitle=placeTitle
        }

    }


}