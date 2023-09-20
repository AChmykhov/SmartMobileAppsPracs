package ru.mirea.chmykhovam.multiactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "onCreate method")
    }

    fun onClickNewActivity(view: View?) {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("key", "No added text in intent")
        startActivity(intent)
    }

    fun onClickSend(view: View?) {
        val editText = findViewById<EditText>(R.id.editTextText)
        val intent = Intent(this@MainActivity, SecondActivity::class.java)
        intent.putExtra("key", editText.text.toString())
        startActivity(intent)
// У второй активности
// У второй активности
        //val text = intent.getStringExtra("key").toString()
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