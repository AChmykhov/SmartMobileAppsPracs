package ru.mirea.chmykhovam.yandexmaps

import android.app.Application
import android.util.Log
import com.yandex.mapkit.MapKitFactory

class App : Application()  {
    private val MAPKIT_API_KEY = BuildConfig.MAPKIT_API_KEY
    override fun onCreate() {
        super.onCreate()
//        Log.d(MainActivity::class.java.simpleName, "MAPKIT_API_KEY")
//        Log.d(MainActivity::class.java.simpleName, MAPKIT_API_KEY)
        // Set the api key before calling initialize on MapKitFactory.
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
    }
}