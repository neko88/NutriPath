package com.group35.nutripath.ui.settings

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.group35.nutripath.MainActivity
import com.group35.nutripath.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (ContextCompat.checkSelfPermission(context!!,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            // create an intent so clicking the notification will redirect to the app
            val appIntent = Intent(context, MainActivity::class.java)
            appIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_IMMUTABLE)

            // Show a notification
            val builder = NotificationCompat.Builder(context, "reminder_channel")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("NutriPath Daily Reminder")
                .setContentText("Don't forget to log your expenses and meals today!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            // Send the notification
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(1, builder.build())
        }
    }
}