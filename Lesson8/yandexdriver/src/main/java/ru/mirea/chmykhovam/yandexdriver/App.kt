package ru.mirea.chmykhovam.yandexdriver

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App: Application()  {
    private val MAPKIT_API_KEY = BuildConfig.MAPKIT_API_KEY
    override fun onCreate() {
        super.onCreate()
        // Set the api key before calling initialize on MapKitFactory.
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
    }
}