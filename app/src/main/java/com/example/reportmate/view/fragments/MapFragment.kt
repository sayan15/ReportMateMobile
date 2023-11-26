package com.example.reportmate.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.reportmate.BuildConfig
import com.example.reportmate.R
import com.example.reportmate.databinding.FragmentMapBinding
import com.example.reportmate.model.latitudeLongitude
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var  navController: NavController
    private var currentMarker: Marker? = null
    private var _binding: FragmentMapBinding? = null
    private lateinit var address: String
    private lateinit var latlng: LatLng
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var nGoogleMap:GoogleMap?=null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(requireContext(), BuildConfig.GOOGLE_MAPS_API_KEY)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    //get refernce to google map
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        autocompleteFragment =  childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place. Field.ID, Place. Field. ADDRESS, Place. Field. LAT_LNG))

        autocompleteFragment.setCountries("Uk")

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {

            override fun onError(p0: Status) {
                Toast.makeText(requireContext(),"Some Error in Search", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected (place: Place) {
                address = place.address
                val id = place.id
                latlng = place.latLng!!
                val marker = addMarker(latlng)
                marker.title="$address"
                marker.snippet = "$id"
                val latlng = place.latLng
                zoomOnMap (latlng)
            }
        })

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        //navigate to map fragment
        binding.saveButtonId.setOnClickListener {
            // Check if latlng is initialized
            if (::latlng.isInitialized) {
                val latitudeLongitude = latitudeLongitude(latlng, address)
                if (latitudeLongitude != null) {
                    val bundle = Bundle()
                    bundle.putParcelable("latitudeLongitude", latitudeLongitude)

                    navController = Navigation.findNavController(view)
                    navController.navigate(R.id.action_mapFragment_to_submitReportFragment3, bundle)
                } else {
                    // Show a toast message if latitudeLongitude is null
                    Toast.makeText(
                        requireContext(),
                        "Unable to determine location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                // Show a toast message if latitudeLongitude is null
                Toast.makeText(
                    requireContext(),
                    "Please select your location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun zoomOnMap (latlng: LatLng)
    {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latlng,12f) // 12f -> amount of zoom level
        nGoogleMap?.animateCamera(newLatLngZoom)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        nGoogleMap= googleMap
        // Set the initial camera position to the United Kingdom
        val ukLatLng = LatLng(51.509865, -0.118092) // Latitude and Longitude for the United Kingdom
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(ukLatLng, 6.0f))

        //call addMarker function whhen map is ready
        addMarker(LatLng(52.2387, -0.9026))

        nGoogleMap?.setOnMapClickListener {
            nGoogleMap?.clear()
            addMarker(it)
        }

    }

    //add marker after user searched it
    private fun addMarker (position: LatLng): Marker
    {
        // Remove the old marker if it exists
        currentMarker?.remove()
        val marker = nGoogleMap?.addMarker (MarkerOptions()
            . position (position)
            .title("Marker")
                )
        // Update the currentMarker reference
        currentMarker = marker

        return marker!!
    }

}