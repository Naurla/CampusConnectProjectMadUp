package com.example.campusconnectprojectmad

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope // Used for coroutine scope
import com.example.campusconnectprojectmad.data.AppDatabase
import com.example.campusconnectprojectmad.data.User // The User Entity
import com.example.campusconnectprojectmad.databinding.FragmentSignUpStep3Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpStep3Fragment : Fragment() {

    private var _binding: FragmentSignUpStep3Binding? = null
    private val binding get() = _binding!!
    private lateinit var parentActivity: SignUpActivity
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentActivity = activity as SignUpActivity

        // Initialize the database instance
        database = AppDatabase.getDatabase(requireContext())

        // Load academic data if returning to this fragment
        binding.etCollege.setText(parentActivity.registrationData["college"])
        binding.etProgram.setText(parentActivity.registrationData["program"])

        binding.btnRegister.setOnClickListener {
            if (validateFields()) {
                saveData()
                // Execute the database registration
                registerUserToDatabase()
            }
        }

        binding.imgArrowBack.setOnClickListener {
            parentActivity.navigateBack()
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (binding.etCollege.text.isNullOrBlank()) {
            binding.etCollege.error = "College is required"
            isValid = false
        }
        if (binding.etProgram.text.isNullOrBlank()) {
            binding.etProgram.error = "Program is required"
            isValid = false
        }
        if (password.isNullOrBlank() || password.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            isValid = false
        }
        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Passwords do not match"
            isValid = false
        }
        return isValid
    }

    private fun saveData() {
        // Save final academic and password data to the shared map
        parentActivity.registrationData["college"] = binding.etCollege.text.toString()
        parentActivity.registrationData["program"] = binding.etProgram.text.toString()
        parentActivity.registrationData["password"] = binding.etPassword.text.toString()
    }

    private fun registerUserToDatabase() {
        val data = parentActivity.registrationData

        // ⚠️ Security Note: This is a PLACEHOLDER hash.
        // In a production app, use a proper library like BCrypt for hashing.
        val passwordHash = data["password"] + "_hashed"

        // Construct the User object from all three fragments' data
        val newUser = User(
            studentNumber = data["student_number"] ?: "",
            firstName = data["first_name"] ?: "",
            middleName = data["middle_name"],
            lastName = data["last_name"] ?: "",
            gender = data["gender"] ?: "",
            dob = data["dob"] ?: "",
            email = data["email"] ?: "",
            phoneNumber = data["phone"] ?: "",
            college = data["college"] ?: "",
            program = data["program"] ?: "",
            passwordHash = passwordHash
        )

        // Launch coroutine on the IO dispatcher for database access
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Check if user already exists
                val existingCount = database.userDao().countUserByStudentNumber(newUser.studentNumber)

                if (existingCount > 0) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "User already registered!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Insert the new user
                    database.userDao().insertUser(newUser)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Registration Successful!", Toast.LENGTH_SHORT).show()

                        // Navigate back to the Login screen, clearing the SignUp history
                        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        activity?.finish()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Database Error: Failed to register user.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}