package ru.mirea.chmykhovam.serviceapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class PlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    val CHANNEL_ID = "ForegroundServiceChannel"

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer!!.start()
        mediaPlayer!!.setOnCompletionListener { stopForeground(STOP_FOREGROUND_REMOVE) }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID).setContentText("Playing Merry Go Round of Life")
                .setSmallIcon(R.mipmap.ic_launcher).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText("Test player 42")
                ).setContentTitle("Music Player")
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            CHANNEL_ID, "Chmykhov AM Notification", importance
        )
        channel.description = "MIREA Channel"
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(channel)
        startForeground(1, builder.build())
        mediaPlayer = MediaPlayer.create(this, R.raw.moving_castle_merry_go_round)
        mediaPlayer!!.isLooping = false
    }

    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        mediaPlayer!!.stop()
    }
}