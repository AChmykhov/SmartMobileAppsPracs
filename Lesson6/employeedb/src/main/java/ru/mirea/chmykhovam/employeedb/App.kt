package ru.mirea.chmykhovam.employeedb

import android.app.Application
import android.content.Context
import androidx.room.Room

class App : Application() {
    // Using by lazy so the database and the instance are only created when they're needed
    // rather than when the application starts
    public val database by lazy {
        Room.databaseBuilder(
            this, AppDatabase::class.java, "database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

//    fun getDatabase(): AppDatabase {
//        return database
//    }
//    companion object{
//        val instance by lazy { this }
//
//    }
}