package ru.mirea.chmykhovam.dialog

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.widget.Toast
import java.util.Calendar


class MyTimeDialogFragment
    (context: Context) : TimePickerDialog(
    context, OnTimeSetListener { view, hourOfDay, minute ->
        Toast.makeText(
            context, "Вы выбрали $hourOfDay:$minute", Toast.LENGTH_SHORT
        ).show()
    }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true
)