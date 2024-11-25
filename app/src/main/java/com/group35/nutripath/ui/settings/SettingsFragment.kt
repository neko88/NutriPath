package com.group35.nutripath.ui.settings

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.group35.nutripath.R
import com.group35.nutripath.databinding.FragmentSettingsBinding
import com.group35.nutripath.ui.notifications.NotificationsReceiver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        sharedPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        binding.buttonSetReminder.setOnClickListener {
            showIntervalDialog()
        }

        // edit profile if user clicks the "Edit Profile" button
        binding.buttonProfile.setOnClickListener {
            findNavController().navigate(R.id.settings_to_profile)
        }

        return root
    }

    private fun showIntervalDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_reminder_intervals, null)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radio_group_intervals)
        val buttonSet = dialogView.findViewById<Button>(R.id.btn_set_interval)

        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val savedInterval = sharedPreferences.getLong("reminder_interval", -1L)
        when (savedInterval) {
            4 * AlarmManager.INTERVAL_HOUR -> radioGroup.check(R.id.radio_4h)
            8 * AlarmManager.INTERVAL_HOUR -> radioGroup.check(R.id.radio_8h)
            12 * AlarmManager.INTERVAL_HOUR -> radioGroup.check(R.id.radio_12h)
            AlarmManager.INTERVAL_DAY -> radioGroup.check(R.id.radio_1d)
            -1L -> radioGroup.check(R.id.radio_never)
        }

        buttonSet.setOnClickListener {
            when (radioGroup.checkedRadioButtonId) {
                R.id.radio_4h -> scheduleReminder(4 * AlarmManager.INTERVAL_HOUR)
                R.id.radio_8h -> scheduleReminder(8 * AlarmManager.INTERVAL_HOUR)
                R.id.radio_12h -> scheduleReminder(12 * AlarmManager.INTERVAL_HOUR)
                R.id.radio_1d -> scheduleReminder(AlarmManager.INTERVAL_DAY)
                else -> cancelReminder()
            }
            dialog.dismiss()
        }

        dialog.show()
    }



    private fun scheduleReminder(interval: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong("reminder_interval", interval).apply()

        val intent = Intent(requireContext(), NotificationsReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                                         interval, pendingIntent)

        val nextReminderTime = getNextReminderTime(interval)
        Toast.makeText(activity, "You have chosen to receive reminders now. Your next reminder is at $nextReminderTime.", Toast.LENGTH_LONG).show()
    }

    // cancel the daily reminder
    private fun cancelReminder() {
        if (::pendingIntent.isInitialized) {
            alarmManager.cancel(pendingIntent)
        }

        val editor = sharedPreferences.edit()
        editor.putLong("reminder_interval", -1L).apply() // Save "Never" state

        Toast.makeText(activity, "You will no longer receive reminders now.", Toast.LENGTH_SHORT).show()
    }

    private fun getNextReminderTime(interval: Long): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, interval.toInt())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(calendar.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}