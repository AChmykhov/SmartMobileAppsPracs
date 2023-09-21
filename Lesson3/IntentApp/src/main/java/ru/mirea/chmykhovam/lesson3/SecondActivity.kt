package ru.mirea.chmykhovam.lesson3

//import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow


class SecondActivity : AppCompatActivity() {
//    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val intent = intent
        val string = intent.getStringExtra("time")
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ ${31f.pow(2f).toInt()}, а текущее время $string"

    }
}