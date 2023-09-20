package ru.mirea.chmykhovam.multiactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class SecondActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        val textView = findViewById<TextView>(R.id.textView)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val text = intent.getStringExtra("key").toString()
        textView.text = text

        Log.i(TAG, "onCreate method")
    }
    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart method")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume method")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause method")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop method")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy method")
    }
}