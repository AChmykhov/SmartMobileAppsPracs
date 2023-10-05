package ru.mirea.chmykhovam.data_thread

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.chmykhovam.data_thread.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val runn1 = Runnable { binding.tvInfo.text = "runn1" }
        val runn2 = Runnable { binding.tvInfo.text = "runn2" }
        val runn3 = Runnable { binding.tvInfo.text = "runn3" }
        val t = Thread {
            try {
                Thread.sleep(2000)
                runOnUiThread(runn1)
                Thread.sleep(1000)
                binding.tvInfo.postDelayed(runn3, 2000)
                binding.tvInfo.post(runn2)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        t.start()
    }
}