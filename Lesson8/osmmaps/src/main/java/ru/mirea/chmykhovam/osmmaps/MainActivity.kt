package ru.mirea.chmykhovam.osmmaps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.mirea.chmykhovam.osmmaps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var locationNewOverlay: MyLocationNewOverlay
    private lateinit var mapView: MapView
    private lateinit var binding: ActivityMainBinding
    private var havePermissions = false
    private val REQUEST_CODE_PERMISSION = 501
    private val noRestrictionsPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val POI = hashMapOf<String, GeoPoint>(
        "Вернадского 78" to GeoPoint(55.669956, 37.480225),
        "Стромынка 20" to GeoPoint(55.794295, 37.701571),
        "Вернадского 86" to GeoPoint(55.661728, 37.477904)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(
            applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        mapView = binding.mapView
        val mapController = mapView.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(55.794229, 37.700772)
        mapController.setCenter(startPoint)
        val compassOverlay = CompassOverlay(
            applicationContext, InternalCompassOrientationProvider(
                applicationContext
            ), mapView
        )
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)
        val context: Context = this.applicationContext
        val dm: DisplayMetrics = context.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(mapView)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        mapView.overlays.add(scaleBarOverlay)
        if (checkPermissions(true)) {
            locationNewOverlay =
                MyLocationNewOverlay(GpsMyLocationProvider(applicationContext), mapView)
            locationNewOverlay.enableMyLocation()
            mapView.overlays.add(this.locationNewOverlay)
        }
        for ((index, point) in POI) {
            addMarker(point, index, org.osmdroid.library.R.drawable.marker_default)
        }
    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(
            applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        Configuration.getInstance().save(
            applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        mapView.onPause()
    }

    private fun checkPermissions(request: Boolean = false): Boolean {
        if (havePermissions) return true
        havePermissions = true
        // permissions that do not have sdk restrictions
        for (permission in noRestrictionsPermissions) {
            if (ContextCompat.checkSelfPermission(
                    this, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                havePermissions = false
                if (request) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(permission), REQUEST_CODE_PERMISSION
                    )
                }
            }
        }

        if (Build.VERSION.SDK_INT > 28 && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            havePermissions = false
            if (request) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    REQUEST_CODE_PERMISSION
                )
            }
        }
        return havePermissions
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            havePermissions = true
            for (permission in noRestrictionsPermissions) {
                if (permissions.contains(permission) && grantResults[permissions.indexOf(permission)] != PackageManager.PERMISSION_GRANTED) {
                    havePermissions = false
                }
            }
            if (Build.VERSION.SDK_INT > 28 && permissions.contains(Manifest.permission.ACCESS_BACKGROUND_LOCATION) && grantResults[permissions.indexOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )] != PackageManager.PERMISSION_GRANTED
            ) {
                havePermissions = false
            }
        }
        // re-check permissions if request code was right, but there were no permission of interest

        if (checkPermissions()) {
            locationNewOverlay =
                MyLocationNewOverlay(GpsMyLocationProvider(applicationContext), mapView)
            locationNewOverlay.enableMyLocation()
            mapView.overlays.add(this.locationNewOverlay)
        }
    }

    fun addMarker(geoPoint: GeoPoint, toastText: String, iconResource: Int) {
        val marker = Marker(mapView)
        marker.position = geoPoint
        marker.setOnMarkerClickListener { _, _ ->
            Toast.makeText(
                applicationContext, toastText, Toast.LENGTH_SHORT
            ).show()
            true
        }
        mapView.overlays.add(marker)

        marker.icon = ResourcesCompat.getDrawable(
            resources, iconResource, null
        )

        marker.title = toastText
    }

}