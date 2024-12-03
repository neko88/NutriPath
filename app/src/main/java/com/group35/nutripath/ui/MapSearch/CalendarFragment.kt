package com.group35.nutripath

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
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
    private lateinit var journalEntryButton: Button
    private lateinit var database: TrackingStatsDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        // Initialize the database
        database = TrackingStatsDatabase.getInstance(requireContext())

        // Initialize views
        calendarView = view.findViewById(R.id.calendar_view)
        statsTextView = view.findViewById(R.id.stats_text_view)
        backButton = view.findViewById(R.id.back_to_map_button)
        journalEntryButton = view.findViewById(R.id.journal_entry_button)

        // Back button listener
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Initially hide the button
        journalEntryButton.visibility = View.GONE

        // Calendar date change listener
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            fetchStatsForDate(selectedDate)
            journalEntryButton.visibility = View.VISIBLE // Show button when a date is selected

            // Handle button click
            journalEntryButton.setOnClickListener {
                showJournalDialog(selectedDate)
            }
        }

        return view
    }

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

    private fun displayStats(date: String, stats: TrackingStats) {
        statsTextView.text = "Date: $date\nTime: ${stats.time}\nDistance: ${stats.distance.roundToInt()}m\nCalories: ${stats.calories}"
    }

    private fun showJournalDialog(date: String) {
        val dialog = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext()).apply {
            hint = "Write your journal entry here..."
        }
        dialog.setTitle("Journal Entry for $date")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                saveJournalForDate(date, input.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveJournalForDate(date: String, journal: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val stats = database.trackingStatsDao().getStatsForDate(date)
                if (stats != null) {
                    database.trackingStatsDao().updateTrackingStats(
                        date = date,
                        time = stats.time,
                        distance = stats.distance,
                        calories = stats.calories,
                        journal = journal
                    )
                } else {
                    database.trackingStatsDao().insertTrackingStats(
                        TrackingStats(
                            date = date,
                            time = "00:00",
                            distance = 0.0,
                            calories = 0,
                            journal = journal
                        )
                    )
                }
            }
        }
    }
}