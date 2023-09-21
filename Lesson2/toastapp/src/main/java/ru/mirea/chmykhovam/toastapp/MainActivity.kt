package ru.mirea.chmykhovam.toastapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickMakeToast(view: View?){
        val editTextLen = findViewById<EditText>(R.id.editText).text.length
        val toast = Toast.makeText(
            applicationContext,
            "СТУДЕНТ № 31 ГРУППА БСБО-05-21 Количество символов - $editTextLen",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }
}