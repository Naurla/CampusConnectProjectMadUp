package com.example.campusconnectprojectmad

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.campusconnectprojectmad.databinding.FragmentSignUpStep2Binding
import java.util.Calendar // Import Calendar for date handling
import java.text.SimpleDateFormat // Import SimpleDateFormat for date formatting
import java.util.Locale // Import Locale

class SignUpStep2Fragment : Fragment() {

    private var _binding: FragmentSignUpStep2Binding? = null
    private val binding get() = _binding!!
    private lateinit var parentActivity: SignUpActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        // Set up gender radio buttons
        when (parentActivity.registrationData["gender"]) {
            "Man" -> binding.rbMale.isChecked = true
            "Woman" -> binding.rbFemale.isChecked = true
            "Other" -> binding.rbOther.isChecked = true
        }

        // 💡 NEW: Attach the DatePicker dialog launcher to the Date of Birth field
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
        // Get current date to set as default date in the picker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // This callback is triggered when the user selects a date

                // Create a calendar instance for the selected date
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)

                // Format the date into a readable string (e.g., DD/MM/YYYY)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                val formattedDate = dateFormat.format(selectedDateCalendar.time)

                // Set the formatted date to the EditText field
                binding.etDob.setText(formattedDate)
            },
            year,
            month,
            day
        )

        // Optional: Prevent users from selecting a date in the future
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (binding.rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(context, "Please select Gender", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (binding.etDob.text.isNullOrBlank()) {
            binding.etDob.error = "Date of Birth is required"
            isValid = false
        }
        if (binding.etEmail.text.isNullOrBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
            binding.etEmail.error = "Valid email required"
            isValid = false
        }
        if (binding.etPhone.text.isNullOrBlank()) {
            binding.etPhone.error = "Phone number is required"
            isValid = false
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
        parentActivity.registrationData["dob"] = binding.etDob.text.toString()
        parentActivity.registrationData["email"] = binding.etEmail.text.toString()
        parentActivity.registrationData["phone"] = binding.etPhone.text.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}