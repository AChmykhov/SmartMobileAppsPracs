package ru.mirea.chmykhovam.systemintentsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickCall(view: View?) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:+74996008080")
        startActivity(intent)
    }

    fun onClickOpenBrowser(view: View?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("http://mirea.ru")
        startActivity(intent)
    }

    fun onClickOpenMaps(view: View?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("geo:55.669956, 37.480225")
        startActivity(intent)
    }
}