package ru.mirea.chmykhovam.employeedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = (application as App).database
        val employeeDao = db.employeeDao()
        var employee: Employee? = Employee(1, "John Smith", 10000)
        employeeDao!!.insert(employee)
        val employees = employeeDao.all
        employee = employeeDao.getById(1)
        employee!!.salary = 20000
        employeeDao.update(employee)

        Log.d(this::class.simpleName, employee.name + " " + employee.salary)
    }
}