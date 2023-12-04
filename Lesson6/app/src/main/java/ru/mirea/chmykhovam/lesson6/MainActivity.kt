package ru.mirea.chmykhovam.lesson6

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.mirea.chmykhovam.lesson6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val filmKey: String = "FILM"
    private val numberKey: String = "NUMBER"
    private val groupKey: String = "GROUP"
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefs = getSharedPreferences("mirea_settings", Context.MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.editTextGroup.setText(sharedPrefs.getString(groupKey, ""))
        binding.editTextNumber.setText(sharedPrefs.getString(numberKey, ""))
        binding.editTextFilm.setText(sharedPrefs.getString(filmKey, ""))
        setContentView(binding.root)
    }

    fun onClickSaveText(v: View) {
        with(sharedPrefs.edit()) {
            putString(groupKey, binding.editTextGroup.text.toString())
            putString(numberKey, binding.editTextNumber.text.toString())
            putString(filmKey, binding.editTextFilm.text.toString())
            apply()
        }
    }
//    companion object{
//        const val filmKey: String = "FILM"
//        const val numberKey: String = "NUMBER"
//        const val groupKey: String = "GROUP"
//    }
}