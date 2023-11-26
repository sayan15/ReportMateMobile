package com.example.reportmate.view.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.reportmate.R
import com.example.reportmate.databinding.FragmentFinalBinding
import com.example.reportmate.databinding.FragmentSubmitReportBinding
import com.example.reportmate.model.FinalData
import com.example.reportmate.model.latitudeLongitude
import com.example.reportmate.viewmodel.SubmitReportViewModel
import com.example.reportmate.viewmodel.finalViewModel
import com.google.android.gms.maps.model.LatLng


class FinalFragment : Fragment() {
    private lateinit var  navController: NavController
    private var _binding: FragmentFinalBinding? = null
    private lateinit var viewModel: finalViewModel
    private var latLng: LatLng? = null
    // This property is only valid between onCreateView
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // inflate the layout and bind to the _binding
        _binding = FragmentFinalBinding.inflate(inflater, container, false)
        val view = binding.root
        val finalData = arguments?.getParcelable<FinalData>("finalData")
        // use the latitudeLongitude object
        if (finalData != null) {
            binding.textView1.text=finalData.selectedTime
            binding.textView2.text=finalData.title
            latLng=finalData.latLng
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        //go back to previous window
        binding.backButton.setOnClickListener {

            navController.navigate(R.id.action_finalFragment_to_submitReportFragment)
        }

        //set crimes
        // Sample crime types
        val crimeTypes = arrayOf("Theft", "Assault", "Burglary", "Fraud", "Vandalism")

        // Get the spinner from the layout
        val crimeTypeSpinner: Spinner = binding.crimeTypeSpinner

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, crimeTypes)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        crimeTypeSpinner.adapter = adapter

        //store the data in firebase
        binding.submitbtn.setOnClickListener {
            // Initialize ViewModel
            viewModel = ViewModelProvider(this).get(finalViewModel::class.java)
            val cleanedPhoneNumber = binding.phoneNumberEditText.text.toString().replace("\\D".toRegex(), "")
            val numericPhone = if (cleanedPhoneNumber.isNotEmpty()) cleanedPhoneNumber.toLong() else 0L


            viewModel.insertData(binding.textView2.text.toString(), latLng?.latitude,latLng?.longitude,binding.descriptionEditText.text.toString(),binding.textView1.text.toString(),0,"no",binding.crimeTypeSpinner.selectedItem.toString(),numericPhone.toInt(),""){ isSuccess,newIncidentKey ->
                if (isSuccess) {
                    Toast.makeText(context, "Info: You have submitted your report successfully", Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                        show()
                    }
                    //pass the data inserted key
                    val bundle = Bundle()
                    bundle.putString("key_value", newIncidentKey)
                    navController.navigate(R.id.action_finalFragment_to_selectImagesFragment,bundle)
                } else {
                    Toast.makeText(context, "Error: Unable to submit your report", Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                        show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}