package com.group35.nutripath

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.group35.nutripath.ui.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class TrackingFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var statsTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var calendarButton: Button
    private lateinit var timerHandler: Handler
    private lateinit var database: TrackingStatsDatabase

    private var isTracking = false
    private var pathPoints = mutableListOf<LatLng>()
    private var currentLocationMarker: Marker? = null

    private var startTime = 0L
    private var elapsedTime = 0L
    private var distanceTravelled = 0.0
    private var caloriesBurned = 0

    // handle location permission request
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            moveToCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // initialize fused location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // initialize the database
        database = TrackingStatsDatabase.getInstance(requireContext())

        // get all view components
        startButton = view.findViewById(R.id.start_tracking_button)
        stopButton = view.findViewById(R.id.stop_tracking_button)
        calendarButton = view.findViewById(R.id.calendar_button)
        statsTextView = view.findViewById(R.id.stats_text_view)

        timerHandler = Handler(Looper.getMainLooper())

        // button clicks
        startButton.setOnClickListener { startTrackingSession() }
        stopButton.setOnClickListener { stopTrackingSession() }
        calendarButton.setOnClickListener { findNavController().navigate(R.id.action_tracking_to_calendar) }

        // map fragment setup
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    // called when map is ready
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomGesturesEnabled = false
        map.uiSettings.isScrollGesturesEnabled = false
        map.uiSettings.isRotateGesturesEnabled = false
        map.uiSettings.isTiltGesturesEnabled = false
        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false
        map.uiSettings.isZoomControlsEnabled = false
        map.uiSettings.isMapToolbarEnabled = false
        moveToCurrentLocation()
    }

    // get current location of the user
    private fun moveToCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    if (currentLocationMarker == null) {
                        currentLocationMarker = map.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
                    } else {
                        currentLocationMarker?.position = currentLatLng
                    }
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }.addOnFailureListener {
                Log.e("TrackingFragment", "Failed to get current location")
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // start tracking session
    private fun startTrackingSession() {
        isTracking = true
        pathPoints.clear()
        startTime = System.currentTimeMillis()
        distanceTravelled = 0.0
        caloriesBurned = 0
        statsTextView.text = formatStats(0, 0.0, 0)

        startButton.visibility = View.GONE
        stopButton.visibility = View.VISIBLE
        startTracking()
        startTimer()
    }

    // stop tracking session
    private fun stopTrackingSession() {
        isTracking = false
        stopButton.visibility = View.GONE
        startButton.visibility = View.VISIBLE
        stopTracking()
        stopTimer()

        // save tracking data to database
        saveTrackingData(elapsedTime / 1000, distanceTravelled, caloriesBurned)
    }

    // request location updates
    private fun startTracking() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L).apply {
                setMinUpdateIntervalMillis(2000L)
            }.build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    if (isTracking) {
                        val currentLocation = locationResult.lastLocation
                        currentLocation?.let {
                            val currentLatLng = LatLng(it.latitude, it.longitude)

                            // calculate distance
                            if (pathPoints.isNotEmpty()) {
                                val lastPoint = pathPoints.last()
                                val results = FloatArray(1)
                                Location.distanceBetween(lastPoint.latitude, lastPoint.longitude, currentLatLng.latitude, currentLatLng.longitude, results)
                                distanceTravelled += results[0]
                                caloriesBurned = (distanceTravelled * 0.05).roundToInt()
                            }

                            pathPoints.add(currentLatLng)

                            // add polyline and marker to map
                            map.addPolyline(PolylineOptions().addAll(pathPoints).color(ContextCompat.getColor(requireContext(), R.color.teal_200)).width(5f))

                            if (currentLocationMarker == null) {
                                currentLocationMarker = map.addMarker(MarkerOptions().position(currentLatLng).title("Your Location"))
                            } else {
                                currentLocationMarker?.position = currentLatLng
                            }
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                        }
                    }
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // stop location updates
    private fun stopTracking() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun saveTrackingData(time: Long, distance: Double, calories: Int) {
        val date = getCurrentDate()
        val newTime = formatTime(time)

        lifecycleScope.launch(Dispatchers.IO) {
            val existingStats = database.trackingStatsDao().getStatsForDate(date)
            if (existingStats != null) {
                // Parse the existing time and add new time
                val updatedTime = addTimes(existingStats.time, newTime)
                val updatedDistance = existingStats.distance + distance
                val updatedCalories = existingStats.calories + calories

                // Update the record in the database
                database.trackingStatsDao().updateTrackingStats(
                    date = date,
                    time = updatedTime,
                    distance = updatedDistance,
                    calories = updatedCalories
                )
            } else {
                // Insert a new record if none exists for the date
                val stats = TrackingStats(date, newTime, distance, calories)
                database.trackingStatsDao().insertTrackingStats(stats)
            }

            Log.d("TrackingFragment", "Saved data for $date - Time: $newTime, Distance: $distance, Calories: $calories")
        }
    }

    // get current date
    private fun getCurrentDate(): String {
        val calendar = java.util.Calendar.getInstance()
        return "${calendar.get(java.util.Calendar.YEAR)}-${calendar.get(java.util.Calendar.MONTH) + 1}-${calendar.get(java.util.Calendar.DAY_OF_MONTH)}"
    }

    // format time
    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    // format stats
    private fun formatStats(time: Long, distance: Double, calories: Int): String {
        return "Time: ${formatTime(time)}\nDistance: ${distance.roundToInt()}m\nCalories: $calories"
    }

    // start timer for tracking
    private fun startTimer() {
        timerHandler.post(object : Runnable {
            override fun run() {
                if (isTracking) {
                    elapsedTime = System.currentTimeMillis() - startTime
                    statsTextView.text = formatStats(elapsedTime / 1000, distanceTravelled, caloriesBurned)
                    timerHandler.postDelayed(this, 1000)
                }
            }
        })
    }

    // stop the timer
    private fun stopTimer() {
        timerHandler.removeCallbacksAndMessages(null)
    }

    //helper function for time addition
    private fun addTimes(time1: String, time2: String): String {
        val parts1 = time1.split(":").map { it.toInt() }
        val parts2 = time2.split(":").map { it.toInt() }

        val totalSeconds = parts1[2] + parts2[2]
        val totalMinutes = parts1[1] + parts2[1] + totalSeconds / 60
        val totalHours = parts1[0] + parts2[0] + totalMinutes / 60

        return String.format("%02d:%02d:%02d", totalHours, totalMinutes % 60, totalSeconds % 60)
    }
}