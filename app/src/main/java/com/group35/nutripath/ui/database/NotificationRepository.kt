package com.group35.nutripath.ui.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotificationRepository(private val notificationDao: NotificationDao) {
    val allNotifications: Flow<List<Notification>> = notificationDao.getAllNotifications()

    fun insertNotification(notification: Notification) {
        CoroutineScope(IO).launch {
            notificationDao.insertNotification(notification)
        }
    }

    fun deleteNotification(id: Long) {
        CoroutineScope(IO).launch {
            notificationDao.deleteNotification(id)
        }
    }

    fun deleteAllNotifications() {
        CoroutineScope(IO).launch {
            notificationDao.deleteAllNotifications()
        }
    }
}