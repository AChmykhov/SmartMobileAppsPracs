package ru.mirea.chmykhovam.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import java.util.Calendar

class MyDateDialogFragment(context: Context) : DatePickerDialog(
    context, OnDateSetListener { view, year, month, dayOfMonth ->
        Toast.makeText(
            context, "Вы выбрали $dayOfMonth.$month.$year", Toast.LENGTH_SHORT
        ).show()
    }, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH
)