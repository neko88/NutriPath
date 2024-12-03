package com.group35.nutripath.ui.MapSearch

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.group35.nutripath.ui.database.TrackingStats
import com.group35.nutripath.ui.database.TrackingStatsDatabase
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class TrackingViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TrackingStatsDatabase.getInstance(application).trackingStatsDao()

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long> = _elapsedTime

    private val _distanceTravelled = MutableLiveData<Double>()
    val distanceTravelled: LiveData<Double> = _distanceTravelled

    private val _caloriesBurned = MutableLiveData<Int>()
    val caloriesBurned: LiveData<Int> = _caloriesBurned

    private val _isTracking = MutableLiveData<Boolean>()
    val isTracking: LiveData<Boolean> = _isTracking

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?> = _currentLocation

    private val pathPoints = mutableListOf<Location>()
    private var startTime = 0L
    private var timerJob: Job? = null

    private var locationCallback: LocationCallback? = null

    init {
        _elapsedTime.value = 0L
        _distanceTravelled.value = 0.0
        _caloriesBurned.value = 0
        _isTracking.value = false
    }

    fun startTracking() {
        if (_isTracking.value == true) return

        if (ContextCompat.checkSelfPermission(
                getApplication<Application>().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        _isTracking.value = true
        startTime = System.currentTimeMillis()

        // Start the timer
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            while (_isTracking.value == true) {
                _elapsedTime.postValue(System.currentTimeMillis() - startTime)
                delay(1000)
            }
        }

        // Start location updates
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(2000L)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val newLocation = locationResult.lastLocation ?: return
                if (pathPoints.isNotEmpty()) {
                    val lastPoint = pathPoints.last()
                    val results = FloatArray(1)
                    Location.distanceBetween(
                        lastPoint.latitude, lastPoint.longitude,
                        newLocation.latitude, newLocation.longitude, results
                    )
                    val distance = results[0]
                    _distanceTravelled.postValue((_distanceTravelled.value ?: 0.0) + distance)
                    _caloriesBurned.postValue((_distanceTravelled.value ?: 0.0 * 0.05).roundToInt())
                }
                pathPoints.add(newLocation)
                _currentLocation.postValue(newLocation)
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            getApplication<Application>().mainLooper
        )
    }

    fun stopTracking() {
        _isTracking.value = false

        timerJob?.cancel()

        locationCallback?.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
        }
        locationCallback = null
    }

    fun getPathPoints(): List<Location> = pathPoints

    fun stopTrackingAndSave(journal: String? = null) {
        stopTracking()

        viewModelScope.launch(Dispatchers.IO) {
            val date = getCurrentDate()
            val elapsedTimeFormatted = formatElapsedTime(_elapsedTime.value ?: 0L)
            val distance = _distanceTravelled.value ?: 0.0
            val calories = _caloriesBurned.value ?: 0

            val existingStats = database.getStatsForDate(date)
            if (existingStats != null) {
                // Update existing stats
                val updatedTime = addTimes(existingStats.time, elapsedTimeFormatted)
                val updatedDistance = existingStats.distance + distance
                val updatedCalories = existingStats.calories + calories
                database.updateTrackingStats(
                    date = date,
                    time = updatedTime,
                    distance = updatedDistance,
                    calories = updatedCalories,
                    journal = journal ?: existingStats.journal.orEmpty()
                )
            } else {
                // Insert new stats
                database.insertTrackingStats(
                    TrackingStats(
                        date = date,
                        time = elapsedTimeFormatted,
                        distance = distance,
                        calories = calories,
                        journal = journal
                    )
                )
            }

            // Reset tracking stats in the ViewModel
            withContext(Dispatchers.Main) {
                _elapsedTime.postValue(0L)
                _distanceTravelled.postValue(0.0)
                _caloriesBurned.postValue(0)
            }
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun formatElapsedTime(elapsedTime: Long): String {
        val hours = elapsedTime / 1000 / 3600
        val minutes = (elapsedTime / 1000 % 3600) / 60
        val seconds = elapsedTime / 1000 % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun addTimes(time1: String, time2: String): String {
        val parts1 = time1.split(":").map { it.toInt() }
        val parts2 = time2.split(":").map { it.toInt() }

        val totalSeconds = parts1[2] + parts2[2]
        val totalMinutes = parts1[1] + parts2[1] + totalSeconds / 60
        val totalHours = parts1[0] + parts2[0] + totalMinutes / 60

        return String.format("%02d:%02d:%02d", totalHours, totalMinutes % 60, totalSeconds % 60)
    }
}