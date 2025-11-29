package com.example.campusconnectprojectmad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.campusconnectprojectmad.databinding.FragmentSignUpStep1Binding


class SignUpStep1Fragment : Fragment() {

    private var _binding: FragmentSignUpStep1Binding? = null
    private val binding get() = _binding!!
    private lateinit var parentActivity: SignUpActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Ensure the fragment is attached to the host activity
        parentActivity = activity as SignUpActivity

        // Load data if available
        binding.etStudentNumber.setText(parentActivity.registrationData["student_number"])
        binding.etFirstName.setText(parentActivity.registrationData["first_name"])
        binding.etMiddleName.setText(parentActivity.registrationData["middle_name"])
        binding.etLastName.setText(parentActivity.registrationData["last_name"])

        binding.imgArrowNext.setOnClickListener {
            if (validateFields()) {
                saveData()
                parentActivity.navigateNext()
            }
        }

        // Back button is intentionally hidden on Step 1 in the XML, but the logic handles navigation
        binding.imgArrowBack.setOnClickListener {
            parentActivity.navigateBack()
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        // Clear previous errors first (optional, but good practice)
        binding.etStudentNumber.error = null
        binding.etFirstName.error = null
        binding.etLastName.error = null

        if (binding.etStudentNumber.text.isNullOrBlank()) {
            binding.etStudentNumber.error = "Student Number is required"
            isValid = false
        }
        if (binding.etFirstName.text.isNullOrBlank()) {
            binding.etFirstName.error = "First Name is required"
            isValid = false
        }
        // Middle name validation is excluded assuming it's optional per your design.
        if (binding.etLastName.text.isNullOrBlank()) {
            binding.etLastName.error = "Last Name is required"
            isValid = false
        }
        return isValid
    }

    private fun saveData() {
        parentActivity.registrationData["student_number"] = binding.etStudentNumber.text.toString()
        parentActivity.registrationData["first_name"] = binding.etFirstName.text.toString()
        parentActivity.registrationData["middle_name"] = binding.etMiddleName.text.toString()
        parentActivity.registrationData["last_name"] = binding.etLastName.text.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}