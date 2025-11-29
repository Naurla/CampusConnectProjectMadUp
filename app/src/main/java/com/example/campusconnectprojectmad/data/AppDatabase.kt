package com.example.campusconnectprojectmad.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The main database class, responsible for connecting all entities and DAOs.
 * Version 1 indicates the initial schema version.
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Abstract function to get the DAO (Data Access Object)
    abstract fun userDao(): UserDao

    companion object {
        // INSTANCE is a Singleton to prevent multiple instances of the database opening.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If INSTANCE is not null, return it, otherwise create a new database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "campus_connect_db" // The database file name
                )
                    // .fallbackToDestructiveMigration() // Optional: Useful during development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}