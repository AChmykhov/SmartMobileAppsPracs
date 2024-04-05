package ru.mirea.chmykhovam.yandexdriver

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CompositeIcon
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import ru.mirea.chmykhovam.yandexdriver.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), UserLocationObjectListener,
    DrivingSession.DrivingRouteListener {
    private val REQUEST_CODE_PERMISSION = 502
    private lateinit var mapView: MapView
    private lateinit var mapKit: MapKit
    private lateinit var binding: ActivityMainBinding
    private var havePermissions = false
    private var userLocationLayer: UserLocationLayer? = null
    private var ROUTE_START_LOCATION = Point(55.670005, 37.479894)
    private val ROUTE_END_LOCATION = Point(55.794306, 37.701574)
    private var SCREEN_CENTER = Point(
        (ROUTE_START_LOCATION.latitude + ROUTE_END_LOCATION.latitude) / 2,
        (ROUTE_START_LOCATION.longitude + ROUTE_END_LOCATION.longitude) / 2
    )
    private lateinit var mapObjects: MapObjectCollection
//    private lateinit var polylines: MapObjectCollection
    private lateinit var drivingRouter: DrivingRouter
    private lateinit var drivingSession: DrivingSession
    private val routesColors = intArrayOf(
        0xFFFF0000.toInt(), 0xFF00FF00.toInt(), 0x00FFBBBB, 0xFF0000FF.toInt()
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        DirectionsFactory.initialize(this)
        mapKit = MapKitFactory.getInstance()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = binding.mapview
        mapView.map.isRotateGesturesEnabled = false
        mapView.map.move(
            CameraPosition(
                SCREEN_CENTER, 11.0f, 0.0f, 0.0f
            ), Animation(Animation.Type.SMOOTH, 0f), null
        )
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        mapObjects = mapView.map.mapObjects.addCollection()
//        mapObjects = mapView.map.mapObjects
//        polylines = mapObjects.addCollection()
        checkAndRequestPermissions()
        if (havePermissions) {
            loadUserLocationLayer()
        }
        submitRequest()
        mapKit.createLocationManager().subscribeForLocationUpdates(
            0.0,
            0,
            0.0,
            true,
            FilteringMode.ON,
            object : LocationListener {
                override fun onLocationUpdated(location: Location) {
                    Log.d(MainActivity::class.java.simpleName, "LocationUpdated " + location.position.longitude)
                    Log.d(MainActivity::class.java.simpleName, "LocationUpdated " + location.position.latitude)
                    ROUTE_START_LOCATION = location.position
                    Log.d(MainActivity::class.java.simpleName,
                        "LocationUpdated $ROUTE_START_LOCATION"
                    )
                    SCREEN_CENTER = Point(
                        (ROUTE_START_LOCATION.latitude + ROUTE_END_LOCATION.latitude) / 2,
                        (ROUTE_START_LOCATION.longitude + ROUTE_END_LOCATION.longitude) / 2
                    )
                    submitRequest()
                }

                override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
                    Log.d(MainActivity::class.java.simpleName, "Location status updated")
                }
            })


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
            ImageProvider.fromBitmap(
                AppCompatResources.getDrawable(
                    this, R.drawable.search_result
                )!!.toBitmap(96, 96)
            ),
            IconStyle().setAnchor(PointF(0.5f, 0.5f)).setRotationType(RotationType.ROTATE)
                .setZIndex(1f).setScale(1f)
        )
        userLocationView.accuracyCircle.fillColor = ColorUtils.setAlphaComponent(Color.BLUE, 153)
        ROUTE_START_LOCATION = userLocationView.pin.geometry
        SCREEN_CENTER = Point(
            (ROUTE_START_LOCATION.latitude + ROUTE_END_LOCATION.latitude) / 2,
            (ROUTE_START_LOCATION.longitude + ROUTE_END_LOCATION.longitude) / 2
        )
        submitRequest()
    }

    override fun onObjectRemoved(p0: UserLocationView) {
        Log.i(MainActivity::class.java.simpleName, "Object removed")
    }

    override fun onObjectUpdated(userLocationView: UserLocationView, p1: ObjectEvent) {
        ROUTE_START_LOCATION = userLocationView.pin.geometry
        SCREEN_CENTER = Point(
            (ROUTE_START_LOCATION.latitude + ROUTE_END_LOCATION.latitude) / 2,
            (ROUTE_START_LOCATION.longitude + ROUTE_END_LOCATION.longitude) / 2
        )
        Log.d(MainActivity::class.java.simpleName, "Object updated $ROUTE_START_LOCATION")
        submitRequest()
    }

    override fun onDrivingRoutes(list: List<DrivingRoute>) {
        Log.d(MainActivity::class.java.simpleName, "Got driving routes $ROUTE_START_LOCATION")
        // убираем старые маршруты
//        polylines.clear()
        mapObjects.clear()
        var color: Int
        for (i in list.indices) {
            // настроиваем цвета для каждого маршрута
            color = routesColors[i]
            // добавляем маршрут на карту
//            polylines.addPolyline(list[i].geometry).setStrokeColor(color)
            mapObjects.addPolyline(list[i].geometry).setStrokeColor(color)
        }
        val placemark = mapObjects.addPlacemark(
            ROUTE_END_LOCATION, ImageProvider.fromBitmap(
                AppCompatResources.getDrawable(this, R.drawable.flag)!!.toBitmap(96, 96)
            )
        )
        placemark.addTapListener { _, _ ->
            Toast.makeText(
                this@MainActivity, "RTU MIREA campus on Stromynka street", Toast.LENGTH_SHORT
            ).show()
            true
        }
    }


    override fun onDrivingRoutesError(error: Error) {
        var errorMessage = "Routes request unknown error"
        if (error is RemoteError) {
            errorMessage = "Routes request error due issues on remote site"
        } else if (error is NetworkError) {
            errorMessage = "Routes request error due network issues"
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun submitRequest() {
        Log.d(MainActivity::class.java.simpleName, "Submit request started $ROUTE_START_LOCATION")
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()
        // Кол-во альтернативных путей
        drivingOptions.routesCount = 4
        val requestPoints: ArrayList<RequestPoint> = ArrayList()
        // Установка точек маршрута
        requestPoints.add(RequestPoint(ROUTE_START_LOCATION, RequestPointType.WAYPOINT, null))
        requestPoints.add(RequestPoint(ROUTE_END_LOCATION, RequestPointType.WAYPOINT, null))
        // Отправка запроса на сервер
        drivingSession =
            drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)
        Log.d(MainActivity::class.java.simpleName, "Submit request finished $ROUTE_START_LOCATION")

    }
}