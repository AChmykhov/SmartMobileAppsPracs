package ru.mirea.chmykhovam.audiorecord

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.mirea.chmykhovam.audiorecord.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var isStartPlaying = true
    private val REQUEST_CODE_PERMISSION = 201
    private var isWork = false
    private lateinit var binding: ActivityMainBinding
    private var isStartRecording = true
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var recordFilePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recordFilePath = File(
            getExternalFilesDir(Environment.DIRECTORY_MUSIC), "/audiorecordtestachm.3gp"
        ).absolutePath
    }

    fun onClickRecording(v: View) {
        if (isStartRecording) {
            binding.btnStartRecord.text = getString(R.string.stop_recording)
            binding.btnPlay.isEnabled = false
            startRecording()
        } else {
            binding.btnStartRecord.text = getString(R.string.start_recording)
            binding.btnPlay.isEnabled = true
            stopRecording()
        }
        isStartRecording = !isStartRecording
    }

    fun onClickPlay(v: View) {
        if (isStartPlaying) {
            binding.btnPlay.text = getString(R.string.stop)
            binding.btnStartRecord.isEnabled = false
            startPlaying()
        } else {
            binding.btnPlay.text = getString(R.string.play)
            binding.btnStartRecord.isEnabled = true
            stopPlaying()
        }
        isStartPlaying = !isStartPlaying
    }

    private fun startRecording() {
        val audioRecordPermissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        if (audioRecordPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_CODE_PERMISSION
            )
        } else {
            isWork = true
        }
        if (isWork) {
            recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(this)
            } else {
                MediaRecorder()
            }
            recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            recorder?.setOutputFile(recordFilePath)
            recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                recorder!!.prepare()
            } catch (e: IOException) {
                Log.e(MainActivity::class.java.simpleName, "prepare() failed")
            }
            recorder?.start()
        } else {
            Log.e(MainActivity::class.java.simpleName, "No permissions")
        }
    }

    private fun stopRecording() {
        recorder?.stop()
        recorder?.release()
        recorder = null
    }

    private fun startPlaying() {
        player = MediaPlayer()
        try {
            player!!.setDataSource(recordFilePath)
            player!!.prepare()
            player!!.start()
        } catch (e: IOException) {
            Log.e(MainActivity::class.java.simpleName, "prepare() failed")
        }
    }

    private fun stopPlaying() {
        player!!.release()
        player = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
// permission granted
            isWork =
                permissions.contains(Manifest.permission.RECORD_AUDIO) && grantResults[permissions.indexOf(
                    Manifest.permission.RECORD_AUDIO
                )] == PackageManager.PERMISSION_GRANTED
        }
    }
}