package ru.mirea.chmykhovam.employeedb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EmployeeDao {
    @get:Query("SELECT * FROM employee")
    val all: List<Employee?>?

    @Query("SELECT * FROM employee WHERE id = :id")
    fun getById(id: Long): Employee?

    @Insert
    fun insert(employee: Employee?)

    @Update
    fun update(employee: Employee?)

    @Delete
    fun delete(employee: Employee?)
}