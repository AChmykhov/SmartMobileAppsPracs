package ru.mirea.chmykhovam.employeedb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Employee::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao?
}