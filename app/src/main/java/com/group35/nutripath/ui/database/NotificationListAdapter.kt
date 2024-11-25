package com.group35.nutripath.ui.database

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.group35.nutripath.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationListAdapter(private val context: Context, private var notifications: List<Notification>) : BaseAdapter() {

    override fun getCount(): Int = notifications.size

    // display items in reverse order
    override fun getItem(position: Int): Notification = notifications[notifications.size - 1 - position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.layout_notification_adapter,null)

        val notification = getItem(position)

        val titleTextView: TextView = view.findViewById(R.id.notification_title)
        val contentTextView: TextView = view.findViewById(R.id.notification_content)
        val timeTextView: TextView = view.findViewById(R.id.notification_time)

        titleTextView.text = notification.title
        timeTextView.text = formatTimestamp(notification.timestamp)
        contentTextView.text = notification.content

        return view
    }

    private fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }

    fun replace(newList: List<Notification>) {
        notifications = newList
    }
}