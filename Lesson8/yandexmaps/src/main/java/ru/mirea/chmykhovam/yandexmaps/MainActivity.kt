package ru.mirea.chmykhovam.yandexmaps


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CompositeIcon
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import ru.mirea.chmykhovam.yandexmaps.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), UserLocationObjectListener {
    private val REQUEST_CODE_PERMISSION = 503
    private lateinit var mapView: MapView
    private lateinit var binding: ActivityMainBinding
    private var havePermissions = false
    private var userLocationLayer: UserLocationLayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.d(MainActivity::class.java.simpleName, BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = binding.mapview
        mapView.map.move(
            CameraPosition(
                Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f
            ), Animation(Animation.Type.SMOOTH, 0f), null
        )
        checkAndRequestPermissions()
        if (havePermissions) {
            loadUserLocationLayer()
        }
    }

    override fun onStop() {
        super.onStop()
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    private fun checkAndRequestPermissions() {
        val coarsePermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val finePermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val backgroundPermission =
            Build.VERSION.SDK_INT < 29 || (Build.VERSION.SDK_INT > 28 && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        if (!coarsePermission) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_PERMISSION
            )
        }
        if (!finePermission) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_PERMISSION
            )
        }
        if (Build.VERSION.SDK_INT > 28 && !backgroundPermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                REQUEST_CODE_PERMISSION
            )
        }
        havePermissions = coarsePermission && finePermission && backgroundPermission
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
// permission granted
            havePermissions =
                permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[permissions.indexOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )] == PackageManager.PERMISSION_GRANTED && permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[permissions.indexOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )] == PackageManager.PERMISSION_GRANTED && (Build.VERSION.SDK_INT < 29 || (Build.VERSION.SDK_INT > 28 && permissions.contains(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) && grantResults[permissions.indexOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )] == PackageManager.PERMISSION_GRANTED))
        }
        if (havePermissions) {
            loadUserLocationLayer()
        }
    }

    private fun loadUserLocationLayer() {
        val mapKit = MapKitFactory.getInstance()
        mapKit.resetLocationManagerToDefault()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer!!.isVisible = true
        userLocationLayer!!.isHeadingEnabled = true
        userLocationLayer!!.setObjectListener(this)
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer ?: return
        userLocationLayer!!.setAnchor(
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )
// При определении направления движения устанавливается следующая иконка
        userLocationView.arrow.setIcon(
            ImageProvider.fromBitmap(
                AppCompatResources.getDrawable(
                    this, R.drawable.arrow_up_float
                )!!.toBitmap(96, 96)
            )
        )
// При получении координат местоположения устанавливается следующая иконка
        val pinIcon: CompositeIcon = userLocationView.pin.useCompositeIcon()
        pinIcon.setIcon(
            "pin",
//            ImageProvider.fromResource(this, R.drawable.search_result),
            ImageProvider.fromBitmap(
                AppCompatResources.getDrawable(
                    this, R.drawable.search_result
                )!!.toBitmap(96, 96)
            ),
            IconStyle().setAnchor(PointF(0.5f, 0.5f)).setRotationType(RotationType.ROTATE)
                .setZIndex(1f).setScale(1f)
        )
        userLocationView.accuracyCircle.fillColor = ColorUtils.setAlphaComponent(Color.BLUE, 153)
    }

    override fun onObjectRemoved(p0: UserLocationView) {
        Log.i(MainActivity::class.java.simpleName, "Object removed")
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
        Log.i(MainActivity::class.java.simpleName, "Object updated")
    }
}