package com.group35.nutripath.ui.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.group35.nutripath.R
import com.group35.nutripath.databinding.FragmentSettingsBinding
import java.util.Calendar

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var switchDailyReminder: SwitchCompat
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
        switchDailyReminder = binding.dailyReminder
        sharedPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        // by default "daily reminder" is disabled, unless user manually enables it
        val isReminderEnabled = sharedPreferences.getBoolean("daily_reminder", false)
        switchDailyReminder.isChecked = isReminderEnabled

        // add/remove the daily reminder if user toggles the switch
        switchDailyReminder.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("daily_reminder", isChecked).apply()

            if (isChecked) {
                scheduleDailyReminder()
            } else {
                cancelDailyReminder()
            }
        }

        // edit profile if user clicks the "Edit Profile" button
        binding.buttonProfile.setOnClickListener {
            findNavController().navigate(R.id.settings_to_profile)
        }

        return root
    }

    private fun scheduleDailyReminder() {
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        // set the alarm to go off at 12 AM
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        // repeat the notification for 24 hours (daily reminder)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                                         AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    // cancel the daily reminder
    private fun cancelDailyReminder() {
        if (::pendingIntent.isInitialized) {
            alarmManager.cancel(pendingIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}