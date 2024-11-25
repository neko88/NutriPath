package com.group35.nutripath.ui.notifications

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.group35.nutripath.R
import com.group35.nutripath.databinding.FragmentNotificationsBinding
import com.group35.nutripath.ui.database.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var database: NotificationDatabase
    private lateinit var databaseDao: NotificationDao
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var notificationViewModelFactory: NotificationViewModelFactory

    private var notificationList: ArrayList<Notification> = ArrayList()
    private lateinit var notificationListAdapter: NotificationListAdapter
    private lateinit var notificationListView: ListView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true) // This enables the options menu

        sharedPreferences = requireActivity().getSharedPreferences("first_launch", Context.MODE_PRIVATE)

        initializeDatabase()

        notificationListView = root.findViewById(R.id.notificationList)
        notificationListAdapter = NotificationListAdapter(requireActivity(), notificationList)
        notificationListView.adapter = notificationListAdapter

        showWelcomeNotification()

        notificationViewModel.allNotificationsLiveData.observe(requireActivity()) {
            notificationListAdapter.replace(it)
            notificationListAdapter.notifyDataSetChanged()
        }

        notificationListView.setOnItemClickListener { _, _, position, _ ->
            val notification = notificationListAdapter.getItem(position)
            showNotificationDialog(notification)
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notification, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notifications -> {
                showDeleteConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeDatabase() {
        database = NotificationDatabase.getInstance(requireActivity())
        databaseDao = database.notificationDao

        notificationRepository = NotificationRepository(databaseDao)
        notificationViewModelFactory = NotificationViewModelFactory(notificationRepository)
        notificationViewModel = ViewModelProvider(this, notificationViewModelFactory)[NotificationViewModel::class.java]
    }

    private fun showWelcomeNotification() {
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)

        if (isFirstLaunch) {
            // If it's the first launch, create a welcome notification
            val welcomeNotification = Notification(
                title = "Welcome to NutriPath!",
                content = "Hello, and welcome to NutriPath!\n\nDon't forget to log your food expenses and track your nutrition!",
                timestamp = System.currentTimeMillis() // Store the current timestamp
            )

            notificationRepository.insertNotification(welcomeNotification)

            // Set "isFirstLaunch" to false to ensure the welcome notification is only created once
            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstLaunch", false)
            editor.apply()
        }
    }

    private fun showNotificationDialog(notification: Notification) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_notification, null)

        val titleTextView = dialogView.findViewById<TextView>(R.id.notification_title)
        val contentTextView = dialogView.findViewById<TextView>(R.id.notification_content)
        val timeTextView = dialogView.findViewById<TextView>(R.id.notification_time)

        titleTextView.text = notification.title
        contentTextView.text = notification.content
        timeTextView.text = formatTimestamp(notification.timestamp)

        builder.setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Delete") { dialog, _ ->
                // Delete the notification from the database when Delete is clicked
                deleteNotification(notification)

                dialog.dismiss()
            }

        builder.create().show()
    }

    private fun deleteNotification(notification: Notification) {
        notificationRepository.deleteNotification(notification.id)

        // Refresh the list after deletion
        notificationViewModel.allNotificationsLiveData.observe(viewLifecycleOwner) { notifications ->
            notificationListAdapter.replace(notifications)
            notificationListAdapter.notifyDataSetChanged()
        }

        Toast.makeText(requireActivity(), "You have deleted this notification.", Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to delete all notifications?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteAllNotifications()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }


    private fun deleteAllNotifications() {
        notificationRepository.deleteAllNotifications()

        // Refresh the list after deletion
        notificationViewModel.allNotificationsLiveData.observe(viewLifecycleOwner) { notifications ->
            notificationListAdapter.replace(notifications)
            notificationListAdapter.notifyDataSetChanged()
        }

        Toast.makeText(requireActivity(), "You have deleted all notifications.", Toast.LENGTH_SHORT).show()
    }

    private fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}