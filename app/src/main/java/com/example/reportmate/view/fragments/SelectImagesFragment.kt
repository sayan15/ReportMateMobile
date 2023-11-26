package com.example.reportmate.view.fragments

import android.app.Activity.RESULT_OK

import android.content.Intent

import android.net.Uri
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.reportmate.R
import com.example.reportmate.databinding.FragmentSelectImagesBinding
import com.example.reportmate.viewmodel.SelectImagesViewModel
import com.example.reportmate.viewmodel.SubmitReportViewModel


class SelectImagesFragment : Fragment() {
    private lateinit var  navController: NavController
    private var _binding: FragmentSelectImagesBinding? = null
    private lateinit var viewModel: SelectImagesViewModel
    // This property is only valid between onCreateView
    private val binding get() = _binding!!

    //store uris of picked images
    private var images: ArrayList<Uri?>? = null
    //current position/index of selected images
    private var position = 0
    //request code to pick image(s)
    private val PICK_IMAGES_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //init list
        images = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout and bind to the _binding
        _binding = FragmentSelectImagesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

        //go back to previous welcome screen window
        binding.discardButton.setOnClickListener {

            navController.navigate(R.id.action_selectImagesFragment_to_welcomeFragment)
        }

        //setup image switcher
        binding.imageSwitcher.setFactory { ImageView(requireContext()) }

        //switch to next image clicking this button
        binding.nextBtn.setOnClickListener {
            if(position<images!!.size-1){
                position++
                binding.imageSwitcher.setImageURI(images!![position])
            }
        }
        //switch to previous image clicking this button
        binding.previousBtn.setOnClickListener {
            if(position>0){
                position--
                binding.imageSwitcher.setImageURI(images!![position])
            }
        }
        //select images
        binding.selectImageButton.setOnClickListener {
            pickImagesIntent()
        }
        //upload images
        binding.uploadImageButton.setOnClickListener {

            val receivedKeyValue = arguments?.getString("key_value")
            if (images != null && images!!.isNotEmpty()) {
                // Filter out null values if any and convert to non-nullable List<Uri>
                val nonNullImages = images!!.filterNotNull()
                // Initialize ViewModel
                viewModel = ViewModelProvider(this).get(SelectImagesViewModel::class.java)
                // Call the ViewModel function to upload all images
                viewModel.uploadImagesToStorage(receivedKeyValue.toString(),nonNullImages){isSuccess->
                    if (isSuccess) {
                        // Image upload successful
                        Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                    } else {
                        // Image upload failed
                        Toast.makeText(requireContext(), "Image upload failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    private fun pickImagesIntent() {
        val intent=Intent()
        intent.type = "image/*"
        intent.putExtra (Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult (Intent.createChooser (intent,  "Select Image(s)"), PICK_IMAGES_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_CODE && resultCode == RESULT_OK) {
            if(data!!.clipData!=null){
                val count=data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    images!!.add(imageUri)
                    position=0
                }
                binding.imageSwitcher.setImageURI(images!![0])
            }else{
                val imageUri=data.data
                images!!.add(imageUri)
                binding.imageSwitcher.setImageURI(imageUri)
                position=0
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}