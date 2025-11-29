package com.example.campusconnectprojectmad.data


import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Defines the 'users' table structure for the Room database.
 * This entity stores all data collected during the three-step registration process.
 */
@Entity(tableName = "users")
data class User(
    // Student Number is the unique identifier (Primary Key) for each user
    @PrimaryKey
    val studentNumber: String,

    val firstName: String,
    val middleName: String?, // Set as nullable to handle optional input
    val lastName: String,
    val gender: String,
    val dob: String,
    val email: String,
    val phoneNumber: String,
    val college: String,
    val program: String,
    // Store a hash/encrypted password, NOT the plain text password
    val passwordHash: String
)