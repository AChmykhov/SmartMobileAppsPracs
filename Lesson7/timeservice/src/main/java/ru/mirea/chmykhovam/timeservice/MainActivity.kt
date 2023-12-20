package ru.mirea.chmykhovam.timeservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.net.Socket
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private val host = "time.nist.gov" // или time.nist.gov

    private val port = 13
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickButton(v: View) {
        lifecycleScope.launch {
            findViewById<TextView>(R.id.textView).text =
                getTimeCoroutine().substring(6,23)
        }
    }

    private suspend fun getTimeCoroutine() = withContext(Dispatchers.IO) {
        var timeResult = ""
        try {
            val socket = Socket(host, port)
            val reader: BufferedReader = SocketUtils.getReader(socket)
            reader.readLine() // игнорируем первую строку
            timeResult = reader.readLine() // считываем вторую строку
            Log.d(MainActivity::class.java.simpleName, timeResult)
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return@withContext timeResult
    }
}