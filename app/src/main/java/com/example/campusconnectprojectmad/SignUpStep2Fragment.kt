package com.example.campusconnectprojectmad

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.campusconnectprojectmad.databinding.FragmentSignUpStep2Binding
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class SignUpStep2Fragment : Fragment() {

    private var _binding: FragmentSignUpStep2Binding? = null
    private val binding get() = _binding!!
    private lateinit var parentActivity: SignUpActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Assume FragmentSignUpStep2Binding now includes et_other_gender and til_other_gender
        _binding = FragmentSignUpStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentActivity = activity as SignUpActivity

        // Load data if available
        binding.etDob.setText(parentActivity.registrationData["dob"])
        binding.etEmail.setText(parentActivity.registrationData["email"])
        binding.etPhone.setText(parentActivity.registrationData["phone"])
        // Load the specific gender text if 'Other' was previously selected
        binding.etOtherGender.setText(parentActivity.registrationData["other_gender"])

        // Set up gender radio buttons
        when (parentActivity.registrationData["gender"]) {
            "Man" -> binding.rbMale.isChecked = true
            "Woman" -> binding.rbFemale.isChecked = true
            "Other" -> {
                binding.rbOther.isChecked = true
                // Show the 'Other Gender' field if 'Other' was loaded
                binding.tilOtherGender.visibility = View.VISIBLE
            }
            else -> {
                // Default: keep the 'Other' field hidden
                binding.tilOtherGender.visibility = View.GONE
            }
        }

        // CRITICAL FIX: Listener for RadioGroup to toggle 'Other' input field visibility
        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == binding.rbOther.id) {
                // If 'Others' is selected, show the input field
                binding.tilOtherGender.visibility = View.VISIBLE
            } else {
                // If 'Man' or 'Woman' is selected, hide the input field and clear it
                binding.tilOtherGender.visibility = View.GONE
                binding.etOtherGender.text?.clear()
                binding.etOtherGender.error = null // Clear any potential error
            }
        }

        // Attach the DatePicker dialog launcher
        binding.etDob.setOnClickListener {
            showDatePickerDialog()
        }

        binding.imgArrowNext.setOnClickListener {
            if (validateFields()) {
                saveData()
                parentActivity.navigateNext()
            }
        }

        binding.imgArrowBack.setOnClickListener {
            parentActivity.navigateBack()
        }
    }

    // Function to launch the native DatePicker dialog
    private fun showDatePickerDialog() {
        // ... (The showDatePickerDialog function remains the same)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                val formattedDate = dateFormat.format(selectedDateCalendar.time)
                binding.etDob.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun validateFields(): Boolean {
        var isValid = true

        // 1. Gender Selection
        if (binding.rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(context, "Please select Gender", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // 2. Validate 'Other Gender' field if 'Other' is selected
        if (binding.rbOther.isChecked && binding.etOtherGender.text.isNullOrBlank()) {
            binding.etOtherGender.error = "Please specify your gender"
            isValid = false
        } else {
            binding.etOtherGender.error = null // Clear error if not applicable/filled
        }

        // 3. Date of Birth
        if (binding.etDob.text.isNullOrBlank()) {
            binding.etDob.error = "Date of Birth is required"
            isValid = false
        } else {
            binding.etDob.error = null
        }

        // 4. Email
        if (binding.etEmail.text.isNullOrBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
            binding.etEmail.error = "Valid email required"
            isValid = false
        } else {
            binding.etEmail.error = null
        }

        // 5. Phone Number
        if (binding.etPhone.text.isNullOrBlank()) {
            binding.etPhone.error = "Phone number is required"
            isValid = false
        } else {
            binding.etPhone.error = null
        }
        return isValid
    }

    private fun saveData() {
        val selectedGender = when (binding.rgGender.checkedRadioButtonId) {
            binding.rbMale.id -> "Man"
            binding.rbFemale.id -> "Woman"
            binding.rbOther.id -> "Other"
            else -> ""
        }
        parentActivity.registrationData["gender"] = selectedGender

        // Save 'other_gender' specification only if 'Other' is selected
        parentActivity.registrationData["other_gender"] = if (binding.rbOther.isChecked) {
            binding.etOtherGender.text.toString()
        } else {
            "" // Store an empty string otherwise
        }

        parentActivity.registrationData["dob"] = binding.etDob.text.toString()
        parentActivity.registrationData["email"] = binding.etEmail.text.toString()
        parentActivity.registrationData["phone"] = binding.etPhone.text.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}