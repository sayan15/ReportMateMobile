package com.example.reportmate.model

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.reportmate.R
import com.example.reportmate.databinding.FragmentViewReportsBinding
import com.example.reportmate.viewmodel.ViewReportsViewModel

class viewReports : Fragment() {
    private lateinit var  navController: NavController
    private var _binding: FragmentViewReportsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = viewReports()
    }

    private lateinit var viewModel: ViewReportsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout and bind to the _binding
        _binding = FragmentViewReportsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ViewReportsViewModel::class.java)
        navController= Navigation.findNavController(view)

        binding.searchButton.setOnClickListener {
            val cleanedPhoneNumber = binding.phoneNumberEditText.text.toString()
            val numericPhone = if (cleanedPhoneNumber.isNotEmpty()) {
                try {
                    cleanedPhoneNumber.toLong()
                } catch (e: NumberFormatException) {
                    // Handle the exception if the string is not a valid integer
                    0 // or any default value you prefer
                }
            } else {
                0 // Default value for empty string
            }


            if(isValidUKPhoneNumber(numericPhone)){
                viewModel.getReports(numericPhone){isSuccess,crimeData->
                    if (isSuccess){ updateTable(crimeData)
                    }
                    else{
                        val tableLayout = binding.tableLayout
                        tableLayout.removeAllViews() // Clear previous data
                    }
                }
            }

        }
        //go back
        binding.backButton.setOnClickListener {
            navController.navigate(R.id.action_viewReports_to_welcomeFragment)
        }

    }
    private fun updateTable(crimeData: List<CrimeData>) {
        val tableLayout = binding.tableLayout
        tableLayout.removeAllViews() // Clear previous data
        val headerRow = TableRow(requireContext())
        val detailHeader = TextView(requireContext()).apply {
            text = "Details"
            gravity = Gravity.CENTER
            background = ContextCompat.getDrawable(requireContext(), R.drawable.table_header)
        }
        val statusHeader = TextView(requireContext()).apply {
            text = "Status"
            gravity = Gravity.CENTER
            background = ContextCompat.getDrawable(requireContext(), R.drawable.table_header)
        }

        headerRow.addView(detailHeader)
        headerRow.addView(statusHeader)
        binding.tableLayout.addView(headerRow)
        // Add a divider after the header
        addDividerToTable(tableLayout)

        crimeData.forEach { record ->
            val row = TableRow(requireContext())
            row.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )


            val descriptionView = TextView(requireContext()).apply {
                text = record.crime_type+" Reported at "+record.title
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.6f)

                gravity = Gravity.CENTER_VERTICAL
            }
            val statusView = TextView(requireContext()).apply {
                text = record.status
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.6f)
                gravity = Gravity.CENTER
            }

            // Add TextViews to the row
            row.addView(descriptionView)
            row.addView(statusView)

            // Add the row to the TableLayout
            tableLayout.addView(row)
            // Add a divider after each row
            addDividerToTable(tableLayout)
        }
    }

    private fun addDividerToTable(tableLayout: TableLayout) {
        val divider = View(requireContext()).apply {
            layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1 // Height of the divider
            )
            background = ContextCompat.getDrawable(requireContext(), R.color.purple_200) // Set the divider color
        }
        tableLayout.addView(divider)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun isValidUKPhoneNumber(phoneNumber: Long): Boolean {
        val numberAsString = phoneNumber.toString()
        val regex = "^\\d{10,11}\$".toRegex()

        return numberAsString.matches(regex)
    }

}