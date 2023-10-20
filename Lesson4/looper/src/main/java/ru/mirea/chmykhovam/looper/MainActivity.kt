package ru.mirea.chmykhovam.looper

import android.os.Bundle
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.chmykhovam.looper.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val myLooper = MyLoooper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myLooper.start()
    }
    fun onButtonClick(v: View){
        val msg: Message = Message.obtain()
        val bundle = Bundle()
        bundle.putString("KEY", binding.editTextProfession.text.toString())
        bundle.putInt("AGE", binding.editTextAge.text.toString().toInt())
        msg.data = bundle
        myLooper.mHandler.sendMessage(msg)
    }
}