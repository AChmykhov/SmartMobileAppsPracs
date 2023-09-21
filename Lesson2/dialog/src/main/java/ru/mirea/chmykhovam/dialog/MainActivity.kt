package ru.mirea.chmykhovam.dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickShowDialog(view: View?) {
        val dialogFragment = MyDialogFragment()
        dialogFragment.show(supportFragmentManager, "mirea")
    }

    fun onOkClicked() {
        Toast.makeText(
            applicationContext, "Вы выбрали кнопку \"Иду дальше\"!", Toast.LENGTH_LONG
        ).show();
    }

    fun onCancelClicked() {
        Toast.makeText(
            applicationContext, "Вы выбрали кнопку \"Нет\"!", Toast.LENGTH_LONG
        ).show();
    }

    fun onNeutralClicked() {
        Toast.makeText(
            applicationContext, "Вы выбрали кнопку \"На паузе\"!", Toast.LENGTH_LONG
        ).show();
    }

    fun onClickShowSnackbar(view: View) {
        val snackbar = Snackbar.make(view, "You opened snackbar", Snackbar.LENGTH_LONG)
        snackbar.setAction("OK") { v: View? ->
            // perform any action when the button on the snackbar is clicked here in this case
            // it shows a simple toast
            Toast.makeText(
                this@MainActivity, "Вы закрыли снакбар нажав ОК", Toast.LENGTH_SHORT
            ).show()
        }

        // the duration is in terms of milliseconds in this case its 3 seconds
        snackbar.duration = 3000
        snackbar.show()
    }

    fun onClickTimePicker(view: View) {
        val timePicker = MyTimeDialogFragment(this)
        timePicker.show()
    }

    fun onClickDatePicker(view: View) {
        val datePicker = MyDateDialogFragment(this)
        datePicker.show()
    }

    fun onClickProgressDialog(view: View) {
        val progressDialog = MyProgressDialogFragment(this)
        progressDialog.show()
    }

}