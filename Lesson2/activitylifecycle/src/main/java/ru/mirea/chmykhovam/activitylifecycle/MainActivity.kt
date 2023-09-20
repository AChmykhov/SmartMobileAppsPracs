package ru.mirea.chmykhovam.activitylifecycle

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "onCreate method")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart method")
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Log.i(TAG, "onRestoreInstanceState method")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart method")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume method")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause method")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.i(TAG, "onSaveInstanceState method")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop method")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy method")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.i(TAG, "onPostCreate method")
    }

    override fun onPostResume() {
        super.onPostResume()
        Log.i(TAG, "onPostResume method")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.i(TAG, "onAttachedToWindow method")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.i(TAG, "onDetachedFromWindow method")
    }
}