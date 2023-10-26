package ru.mirea.chmykhovam.serviceapp

import android.Manifest.permission.FOREGROUND_SERVICE
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.mirea.chmykhovam.serviceapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var buttonState = 0
    private val permissionCode = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ContextCompat.checkSelfPermission(
                this, POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            Log.d(MainActivity::class.java.simpleName, "Разрешения получены")
        } else {
            Log.d(MainActivity::class.java.simpleName, "Нет разрешений!")

            ActivityCompat.requestPermissions(
                this, arrayOf(POST_NOTIFICATIONS, FOREGROUND_SERVICE), permissionCode
            )

        }
    }

    fun onButtonClick(v: View) {
        if (buttonState == 0) {
            val serviceIntent = Intent(this@MainActivity, PlayerService::class.java)
            ContextCompat.startForegroundService(this@MainActivity, serviceIntent)
            binding.buttonPlay.setImageResource(R.drawable.baseline_stop)
            binding.imageAlbumArt.setImageResource(R.drawable.joe_hisaishi_merry_go_round_of_life)
            binding.textTitle.text = getString(R.string.merry_go_round)
            binding.textArtist.text = getString(R.string.howl_s_walking_castle)
            buttonState = 1
        }else if (buttonState == 1){
            stopService(Intent(this@MainActivity, PlayerService::class.java))
            binding.buttonPlay.setImageResource(R.drawable.baseline_play)
            binding.imageAlbumArt.setImageResource(R.mipmap.ic_launcher)
            binding.textTitle.text = getString(R.string.current_song)
            binding.textArtist.text = getString(R.string.artist_name)
            buttonState = 0
        }else{
            throw IllegalStateException("Unknown state code")
        }
    }
}