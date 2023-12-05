package ru.mirea.chmykhovam.securesharedpreferences

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        val secureSharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            mainKeyAlias,
            baseContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        try {
            secureSharedPreferences.edit().putString("secure", "Любимый Актёр - Киану Ривз").apply()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        val result = secureSharedPreferences.getString("secure", "ЛЮБИМЫЙ АКТЕР")
        Log.i(MainActivity::class.java.simpleName, result!!)
        findViewById<TextView>(R.id.textView).text = result
    }
}