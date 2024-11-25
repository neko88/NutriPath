package com.group35.nutripath

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.group35.nutripath.ui.database.TrackingStats
import com.group35.nutripath.ui.database.TrackingStatsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class CalendarFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var statsTextView: TextView
    private lateinit var backButton: Button
    private lateinit var database: TrackingStatsDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        // initialize the database
        database = TrackingStatsDatabase.getInstance(requireContext())

        // initialize views
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
        lifecycleScope.launch {
            val stats = withContext(Dispatchers.IO) {
                database.trackingStatsDao().getStatsForDate(date)
            }
            if (stats != null) {
                displayStats(date, stats)
            } else {
                statsTextView.text = "No tracking data available for $date"
            }
        }
    }

    // display stats in the text view
    private fun displayStats(date: String, stats: TrackingStats) {
        statsTextView.text = "Date: $date\nTime: ${stats.time}\nDistance: ${stats.distance.roundToInt()}m\nCalories: ${stats.calories}"
    }
}