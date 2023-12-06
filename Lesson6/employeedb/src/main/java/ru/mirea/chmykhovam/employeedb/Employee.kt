package ru.mirea.chmykhovam.employeedb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Employee(
    @PrimaryKey(autoGenerate = true) var id: Long, var name: String, var salary: Int
)