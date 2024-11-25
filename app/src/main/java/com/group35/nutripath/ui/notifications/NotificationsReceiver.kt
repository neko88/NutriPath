package com.group35.nutripath.ui.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.group35.nutripath.MainActivity
import com.group35.nutripath.R
import com.group35.nutripath.ui.database.Notification
import com.group35.nutripath.ui.database.NotificationDao
import com.group35.nutripath.ui.database.NotificationDatabase
import com.group35.nutripath.ui.database.NotificationRepository
import com.group35.nutripath.ui.database.NotificationViewModel
import com.group35.nutripath.ui.database.NotificationViewModelFactory

class NotificationsReceiver : BroadcastReceiver() {
    private lateinit var database: NotificationDatabase
    private lateinit var databaseDao: NotificationDao
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var notificationViewModelFactory: NotificationViewModelFactory

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            initializeDatabase(context)

            createNotification(context)
            insertNotificationIntoDatabase(context)
        }
    }

    private fun initializeDatabase(context: Context) {
        database = NotificationDatabase.getInstance(context)
        databaseDao = database.notificationDao

        notificationRepository = NotificationRepository(databaseDao)
        notificationViewModelFactory = NotificationViewModelFactory(notificationRepository)
        notificationViewModel = ViewModelProvider.NewInstanceFactory().create(NotificationViewModel::class.java)
    }

    private fun createNotification(context: Context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            // create an intent so clicking the notification will redirect to the app
            val appIntent = Intent(context, MainActivity::class.java)
            appIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_IMMUTABLE)

            // Show a notification
            val builder = NotificationCompat.Builder(context, "reminder_channel")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("NutriPath Reminder")
                .setContentText("Don't forget to log your food expenses!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            // Send the notification
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(1, builder.build())
        }
    }

    private fun insertNotificationIntoDatabase(context: Context) {
        // Create a new notification object
        val notification = Notification(
            title = "NutriPath Reminder",
            content = "Don't forget to log your food expenses!",
            timestamp = System.currentTimeMillis()
        )

        // Insert into the database
        notificationViewModel.insertNotification(notification)
    }
}