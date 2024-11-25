package com.group35.nutripath

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.group35.nutripath.ui.database.TrackingStats
import kotlin.math.roundToInt


class CalendarFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var statsTextView: TextView
    private lateinit var backButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarView = view.findViewById(R.id.calendar_view)
        statsTextView = view.findViewById(R.id.stats_text_view)
        backButton = view.findViewById(R.id.back_to_map_button)

        // back button listener
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // calendar date change listener
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            fetchStatsForDate(selectedDate)
        }

        return view
    }

    // fetch stats for the selected date
    private fun fetchStatsForDate(date: String) {
        val sharedPrefs = requireContext().getSharedPreferences("tracking_data", Context.MODE_PRIVATE)
        val stats = sharedPrefs.getString(date, null)

        if (stats != null) {
            val trackingStats = Gson().fromJson(stats, TrackingStats::class.java)
            statsTextView.text = "Date: $date\nTime: ${trackingStats.time}\nDistance: ${trackingStats.distance.roundToInt()}m\nCalories: ${trackingStats.calories}"
        } else {
            statsTextView.text = "No tracking data available for $date"
        }
    }
}