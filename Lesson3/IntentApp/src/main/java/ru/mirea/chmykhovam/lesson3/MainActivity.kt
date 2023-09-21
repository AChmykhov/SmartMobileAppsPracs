package ru.mirea.chmykhovam.lesson3

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickSecondActivity(view: View) {
        val dateInMillis = System.currentTimeMillis()
        val format = "yyyy-MM-dd HH:mm:ss"
        val sdf = SimpleDateFormat(format)
        val dateString: String = sdf.format(Date(dateInMillis))
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("time", dateString)
        startActivity(intent)
    }
}