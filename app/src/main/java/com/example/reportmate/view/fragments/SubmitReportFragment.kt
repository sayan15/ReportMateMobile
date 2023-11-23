package com.example.reportmate.view.fragments

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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.reportmate.R
import com.example.reportmate.databinding.FragmentSubmitReportBinding
import com.example.reportmate.viewmodel.BroadCastReceiver
import com.example.reportmate.viewmodel.LocationService
import com.example.reportmate.viewmodel.SubmitReportViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.*


class SubmitReportFragment : Fragment(),BroadCastReceiver.LocationCallback {
    private lateinit var  navController: NavController
    private lateinit var datePicker: DatePicker
    private var _binding: FragmentSubmitReportBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var isReceiverRegistered = false
    private lateinit var viewModel: SubmitReportViewModel
    lateinit var  latLng: LatLng

    private val BroadCastReceiverInstance = BroadCastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // inflate the layout and bind to the _binding
        _binding = FragmentSubmitReportBinding.inflate(inflater, container, false)
        val view = binding.root

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
        //load map
        binding.radioButtonOption2grp1.setOnClickListener {
            // Unregister the receiver in onDestroyView if registered
            if (isReceiverRegistered) {
                requireActivity().unregisterReceiver(BroadCastReceiverInstance.locationReceiver)
                isReceiverRegistered = false
            }

            navController= Navigation.findNavController(view)
            navController.navigate(R.id.action_submitReportFragment_to_mapFragment)

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
            // Set an OnClickListener for the TextView
            binding.timesetter.setOnClickListener {
                showTimePickerDialog();
            }
        } else {
            datePicker.visibility = View.GONE
            binding.timePicker.visibility=View.GONE
        }
    }
    //time picker pop up
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // Create a TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            requireContext(),

            R.style.TimePickerDialogTheme,
            { _, hourOfDay, minute ->
                // Handle the selected time, you can update the TextView or perform other actions
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                binding.timesetter.text = selectedTime
            },
            currentHour,
            currentMinute,
            false // 24-hour format
        )

        // Show the TimePickerDialog
        timePickerDialog.show()
    }

    // Implement the onLocationUpdate function to handle the updated location values
    override fun onLocationUpdate(latitude: Double, longitude: Double) {
        // Perform actions based on the updated location values
        Log.d("LocationUpdate", "Latitude: $latitude, Longitude: $longitude")

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(SubmitReportViewModel::class.java)

        // Call the ViewModel function
        viewModel.getPlaceTitleFromLatLng(requireContext(), LatLng(latitude, longitude))

        // Log the live data value (assuming your LiveData is of type String, adjust accordingly)
        viewModel.resultLiveData.observe(viewLifecycleOwner) { placeTitle ->
            Log.d("LiveDataUpdate", "Place Title: $placeTitle")
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubmitReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubmitReportFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}