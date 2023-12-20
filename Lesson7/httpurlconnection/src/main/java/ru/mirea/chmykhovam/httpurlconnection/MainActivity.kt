package ru.mirea.chmykhovam.httpurlconnection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkinfo = connectivityManager.activeNetworkInfo
        if (networkinfo != null && networkinfo.isConnected) {
            findViewById<TextView>(R.id.textViewIP).text = "Загружаем..."
            lifecycleScope.launch {
                val coordinates = downloadIpCoroutine("https://ipinfo.io/json")
                downloadWeatherCoroutine("https://api.open-meteo.com/v1/forecast?latitude=${coordinates[0]}&longitude=${coordinates[1]}&current_weather=true")
            }
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun downloadWeatherCoroutine(address: String) = withContext(Dispatchers.Main) {
        val data = downloadPageCoroutine(address)
        val responseJson = try {
            val responseJson = JSONObject(data)
            Log.d(
                MainActivity::class.java.simpleName, "Response: $responseJson"
            )
            responseJson
        } catch (e: JSONException) {
            e.printStackTrace()
            JSONObject()
        }
        findViewById<TextView>(R.id.textViewWeather).text =
            responseJson.getString("current_weather")
    }

    private suspend fun downloadIpCoroutine(address: String) = withContext(Dispatchers.Main) {

        val data = downloadPageCoroutine(address)
        //parsing
        val responseJson = try {
            val responseJson = JSONObject(data)
            Log.d(
                MainActivity::class.java.simpleName, "Response: $responseJson"
            )
            responseJson
        } catch (e: JSONException) {
            e.printStackTrace()
            JSONObject()
        }
        findViewById<TextView>(R.id.textViewIP).text = responseJson.getString("ip")
        findViewById<TextView>(R.id.textViewCity).text = responseJson.getString("city")
        findViewById<TextView>(R.id.textViewRegion).text = responseJson.getString("region")
        findViewById<TextView>(R.id.textViewCountry).text = responseJson.getString("country")

        Log.d(
            MainActivity::class.java.simpleName, "IP: $responseJson.getString(\"ip\")"
        )
        return@withContext responseJson.getString("loc").split(",")

    }


    private suspend fun downloadPageCoroutine(address: String) = withContext(Dispatchers.IO) {
        var inputStream: InputStream? = null
        var data = ""
        try {
            val url = URL(address)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.readTimeout = 1000000
            connection.connectTimeout = 1000000
            connection.requestMethod = "GET"
            connection.instanceFollowRedirects = true
            connection.useCaches = false
            connection.doInput = true
            val responseCode: Int = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.inputStream
                val bos = ByteArrayOutputStream()
                var read = 0
                while (inputStream.read().also { read = it } != -1) {
                    bos.write(read)
                }
                bos.close()
                data = bos.toString()
            } else {
                data = connection.responseMessage + ". Error Code: " + responseCode
            }
            connection.disconnect()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return@withContext data
    }


}