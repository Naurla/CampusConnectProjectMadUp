package com.example.campusconnectprojectmad.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    // Inserts a new user or replaces them if the studentNumber already exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // Used for login validation: retrieves user data based on their student number
    @Query("SELECT * FROM users WHERE studentNumber = :studentNumber LIMIT 1")
    suspend fun getUserByStudentNumber(studentNumber: String): User?

    // Checks if a user already exists with the given student number
    @Query("SELECT COUNT(studentNumber) FROM users WHERE studentNumber = :studentNumber")
    suspend fun countUserByStudentNumber(studentNumber: String): Int
}