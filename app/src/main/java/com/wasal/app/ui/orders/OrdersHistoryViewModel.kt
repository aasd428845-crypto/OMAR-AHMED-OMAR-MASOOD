package com.wasal.app.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasal.app.data.model.DeliveryOrder
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

data class OrdersHistoryUiState(
    val isLoading: Boolean = true,
    val orders: List<DeliveryOrder> = emptyList(),
    val error: String? = null,
    val cancellingId: String? = null
)

class OrdersHistoryViewModel : ViewModel() {
    private val repo = OrderRepository()
    private val _uiState = MutableStateFlow(OrdersHistoryUiState())
    val uiState: StateFlow<OrdersHistoryUiState> = _uiState.asStateFlow()

    private var realtimeJob: Job? = null

    fun loadOrders(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val orders = repo.getCustomerOrders(userId)
                _uiState.update { it.copy(isLoading = false, orders = orders) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun subscribeToUpdates(userId: String) {
        realtimeJob?.cancel()
        realtimeJob = viewModelScope.launch {
            runCatching {
                WasalSupabase.realtime.connect()
                val channel = WasalSupabase.realtime.channel("orders-history-$userId")
                channel.postgresChangeFlow<PostgresAction.Update>(schema = "public") {
                    table = "delivery_orders"
                    filter = "customer_id=eq.$userId"
                }.collect {
                    // Refresh orders on any update
                    val orders = runCatching { repo.getCustomerOrders(userId) }.getOrDefault(emptyList())
                    _uiState.update { state -> state.copy(orders = orders) }
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
                    WasalSupabase.realtime.channel("orders-history-$userId")
                )
            }
        }
    }

    fun cancelOrder(orderId: String, userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(cancellingId = orderId) }
            try {
                repo.cancelOrder(orderId)
                val orders = repo.getCustomerOrders(userId)
                _uiState.update { it.copy(orders = orders, cancellingId = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(cancellingId = null, error = e.message) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        realtimeJob?.cancel()
    }
}
