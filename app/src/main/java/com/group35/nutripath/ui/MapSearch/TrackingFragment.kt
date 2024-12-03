package com.group35.nutripath

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.group35.nutripath.ui.MapSearch.TrackingViewModel

class TrackingFragment : Fragment() {

    private lateinit var trackingViewModel: TrackingViewModel
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var statsTextView: TextView
    private lateinit var calendarButton: Button
    private var map: GoogleMap? = null
    private var currentMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Initialize UI components
        startButton = view.findViewById(R.id.start_tracking_button)
        stopButton = view.findViewById(R.id.stop_tracking_button)
        statsTextView = view.findViewById(R.id.stats_text_view)
        calendarButton = view.findViewById(R.id.calendar_button)

        trackingViewModel = ViewModelProvider(this)[TrackingViewModel::class.java]

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            setupMapObservers()
        }

        setupObservers()
        setupListeners()

        return view
    }

    private fun setupObservers() {
        trackingViewModel.elapsedTime.observe(viewLifecycleOwner) { elapsedTime ->
            updateStatsText(
                elapsedTime,
                trackingViewModel.distanceTravelled.value ?: 0.0,
                trackingViewModel.caloriesBurned.value ?: 0
            )
        }

        trackingViewModel.distanceTravelled.observe(viewLifecycleOwner) { distance ->
            updateStatsText(
                trackingViewModel.elapsedTime.value ?: 0L,
                distance,
                trackingViewModel.caloriesBurned.value ?: 0
            )
        }

        trackingViewModel.caloriesBurned.observe(viewLifecycleOwner) { calories ->
            updateStatsText(
                trackingViewModel.elapsedTime.value ?: 0L,
                trackingViewModel.distanceTravelled.value ?: 0.0,
                calories
            )
        }

        trackingViewModel.isTracking.observe(viewLifecycleOwner) { isTracking ->
            if (isTracking) {
                startButton.visibility = View.GONE
                stopButton.visibility = View.VISIBLE
            } else {
                startButton.visibility = View.VISIBLE
                stopButton.visibility = View.GONE
            }
        }
    }

    private fun setupMapObservers() {
        trackingViewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                map?.let { googleMap ->
                    // Update marker
                    if (currentMarker == null) {
                        currentMarker = googleMap.addMarker(MarkerOptions().position(latLng).title("Current Location"))
                    } else {
                        currentMarker?.position = latLng
                    }

                    // Move the camera
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                    // Add trail
                    val pathPoints = trackingViewModel.getPathPoints()
                    if (pathPoints.size > 1) {
                        googleMap.addPolyline(
                            PolylineOptions()
                                .addAll(pathPoints.map { LatLng(it.latitude, it.longitude) })
                                .width(5f)
                                .color(ContextCompat.getColor(requireContext(), R.color.teal_200))
                        )
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        startButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                trackingViewModel.startTracking()
            } else {
                Toast.makeText(requireContext(), "Location permission required", Toast.LENGTH_SHORT).show()
            }
        }

        stopButton.setOnClickListener {
            trackingViewModel.stopTrackingAndSave()
        }

        calendarButton.setOnClickListener {
            findNavController().navigate(R.id.action_tracking_to_calendar)
        }
    }

    private fun updateStatsText(elapsedTime: Long, distance: Double, calories: Int) {
        val seconds = elapsedTime / 1000 % 60
        val minutes = elapsedTime / 1000 / 60 % 60
        val hours = elapsedTime / 1000 / 3600
        statsTextView.text = "Time: %02d:%02d:%02d\nDistance: %.2f m\nCalories: $calories".format(
            hours,
            minutes,
            seconds,
            distance
        )
    }
}