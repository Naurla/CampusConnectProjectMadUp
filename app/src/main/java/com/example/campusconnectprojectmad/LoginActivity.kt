package com.example.campusconnectprojectmad

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campusconnectprojectmad.data.AppDatabase // Import database
import com.example.campusconnectprojectmad.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: AppDatabase // Database instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database instance
        database = AppDatabase.getDatabase(applicationContext)

        // Set up system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLoginSubmit.setOnClickListener {
            handleLogin()
        }

        // Set up navigation to the Sign Up screen
        binding.tvSignUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Handles input validation and database login attempt.
     */
    private fun handleLogin() {
        // Assume 'et_username' is used for studentNumber input
        val studentNumber = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // 1. Input Validation
        if (studentNumber.isEmpty()) {
            binding.etUsername.error = "Student Number is required"
            binding.etUsername.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            binding.etPassword.requestFocus()
            return
        }

        // 2. Database Login Check (Requires CoroutineScope and Dispatchers.IO)
        // Use CoroutineScope tied to the Activity lifecycle (Dispatchers.Main is the default here)
        CoroutineScope(Dispatchers.Main).launch {
            // Move database operation to the background thread (Dispatchers.IO)
            val user = withContext(Dispatchers.IO) {
                database.userDao().getUserByStudentNumber(studentNumber)
            }

            if (user != null) {
                // NOTE: Password checking logic needs to be secure!
                // We use the same 'hash' logic from the registration step for demonstration.
                val expectedPasswordHash = password + "_hashed"

                if (user.passwordHash == expectedPasswordHash) {
                    // Login Successful!
                    Toast.makeText(this@LoginActivity, "Welcome, ${user.firstName}!", Toast.LENGTH_SHORT).show()

                    // Navigate to the main app screen (clearing back stack)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                } else {
                    // Password Mismatch
                    Toast.makeText(this@LoginActivity, "Invalid password.", Toast.LENGTH_LONG).show()
                }
            } else {
                // User not found
                Toast.makeText(this@LoginActivity, "User not found. Please Sign Up.", Toast.LENGTH_LONG).show()
            }
        }
    }
}