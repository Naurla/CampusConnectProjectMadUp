package com.example.campusconnectprojectmad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.campusconnectprojectmad.databinding.FragmentSignUpStep2Binding


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

        binding.imgArrowNext.setOnClickListener {
            if (validateFields()) {
                saveData()
                parentActivity.navigateNext()
            }
        }

        binding.imgArrowBack.setOnClickListener {
            parentActivity.navigateBack()
        }

        // Example of adding a DatePicker listener (if needed)
        binding.etDob.setOnClickListener {
            // Implement DatePicker dialog here
            Toast.makeText(context, "Date Picker placeholder", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (binding.rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(context, "Please select Gender", Toast.LENGTH_SHORT).show()
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