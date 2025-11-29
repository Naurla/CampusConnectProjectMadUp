package com.example.campusconnectprojectmad

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.campusconnectprojectmad.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewPager: ViewPager2

    // 💡 FIX: This line defines the data map that the fragments need to access
    val registrationData = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager = binding.viewPagerSignup
        viewPager.adapter = SignUpPagerAdapter(this)
        // Disable user swipe to force navigation using buttons
        viewPager.isUserInputEnabled = false
    }

    // Function called by fragments to move to the next page
    fun navigateNext() {
        val currentItem = viewPager.currentItem
        if (currentItem < (viewPager.adapter?.itemCount ?: 0) - 1) {
            viewPager.currentItem = currentItem + 1
        }
    }

    // Function called by fragments to move to the previous page
    fun navigateBack() {
        val currentItem = viewPager.currentItem
        if (currentItem > 0) {
            viewPager.currentItem = currentItem - 1
        } else {
            // If on the first page, finish the activity (go back to Landing/Login)
            finish()
        }
    }

    // Function to handle the final registration submission
    fun submitRegistration() {
        // Here you would send the 'registrationData' map to your backend/database.
        Toast.makeText(this, "Registration Successful! Data submitted.", Toast.LENGTH_LONG).show()
        // Navigate back to the login screen
        finish()
    }

    private inner class SignUpPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> SignUpStep1Fragment()
                1 -> SignUpStep2Fragment()
                2 -> SignUpStep3Fragment()
                else -> throw IllegalStateException("Invalid position in ViewPager")
            }
        }
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            navigateBack()
        }
    }
}