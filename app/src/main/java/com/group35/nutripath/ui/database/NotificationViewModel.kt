package com.group35.nutripath.ui.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

class NotificationViewModel(private val repository: NotificationRepository): ViewModel() {
    val allNotificationsLiveData: LiveData<List<Notification>> = repository.allNotifications.asLiveData()

    fun insertNotification(notification: Notification) {
        repository.insertNotification(notification)
    }

    fun deleteNotification(id: Long) {
        repository.deleteNotification(id)
    }

    fun deleteAllNotifications() {
        repository.deleteAllNotifications()
    }
}

class NotificationViewModelFactory(private val repository: NotificationRepository): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java))
            return NotificationViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}