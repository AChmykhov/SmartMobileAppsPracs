package ru.mirea.chmykhovam.dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


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
            applicationContext, "Вы выбрали кнопку \"Иду дальше\"!",
            Toast.LENGTH_LONG
        ).show();
    }

    fun onCancelClicked() {
        Toast.makeText(
            applicationContext, "Вы выбрали кнопку \"Нет\"!",
            Toast.LENGTH_LONG
        ).show();
    }

    fun onNeutralClicked() {
        Toast.makeText(
            applicationContext, "Вы выбрали кнопку \"На паузе\"!",
            Toast.LENGTH_LONG
        ).show();
    }
}