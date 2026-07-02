package com.wasal.app.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasal.app.data.model.AppNotification
import com.wasal.app.data.repository.OrderRepository
import com.wasal.app.data.remote.WasalSupabase
import io.github.jan-tennert.supabase.realtime.PostgresAction
import io.github.jan-tennert.supabase.realtime.realtime
import io.github.jan-tennert.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotificationsUiState(
    val isLoading: Boolean = true,
    val notifications: List<AppNotification> = emptyList(),
    val unreadCount: Int = 0,
    val error: String? = null
)

class NotificationsViewModel : ViewModel() {
    private val repo = OrderRepository()
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private var realtimeJob: Job? = null

    fun loadNotifications(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val notifs = repo.getNotifications(userId)
                _uiState.update { it.copy(
                    isLoading = false,
                    notifications = notifs,
                    unreadCount = notifs.count { it.isUnread }
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun subscribeToNotifications(userId: String) {
        realtimeJob?.cancel()
        realtimeJob = viewModelScope.launch {
            runCatching {
                WasalSupabase.realtime.connect()
                val channel = WasalSupabase.realtime.channel("notif-inbox-$userId")
                channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
                    table = "notifications"
                    filter = "user_id=eq.$userId"
                }.collect { change ->
                    val newNotif = change.decodeRecord<AppNotification>()
                    _uiState.update { state ->
                        val updated = listOf(newNotif) + state.notifications
                        state.copy(
                            notifications = updated,
                            unreadCount = updated.count { it.isUnread }
                        )
                    }
                }
                channel.subscribe()
            }
        }
    }

    fun unsubscribe(userId: String) {
        realtimeJob?.cancel()
        viewModelScope.launch {
            runCatching {
                WasalSupabase.realtime.removeChannel(
                    WasalSupabase.realtime.channel("notif-inbox-$userId")
                )
            }
        }
    }

    fun markAsRead(notif: AppNotification, userId: String) {
        if (!notif.isUnread) return
        val now = java.time.Instant.now().toString()
        _uiState.update { state ->
            val updated = state.notifications.map {
                if (it.id == notif.id) it.copy(readAt = now) else it
            }
            state.copy(notifications = updated, unreadCount = updated.count { it.isUnread })
        }
        viewModelScope.launch {
            runCatching { repo.markNotificationRead(notif.id) }
        }
    }

    fun markAllAsRead(userId: String) {
        val now = java.time.Instant.now().toString()
        _uiState.update { state ->
            val updated = state.notifications.map { it.copy(readAt = it.readAt ?: now) }
            state.copy(notifications = updated, unreadCount = 0)
        }
        viewModelScope.launch {
            runCatching { repo.markAllNotificationsRead(userId) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        realtimeJob?.cancel()
    }
}
