package com.wasal.app.ui.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasal.app.data.model.DeliveryOrder
import com.wasal.app.data.model.Restaurant
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

data class TrackingUiState(
    val isLoading: Boolean = true,
    val order: DeliveryOrder? = null,
    val restaurant: Restaurant? = null,
    val showReviewDialog: Boolean = false,
    val reviewRating: Int = 5,
    val reviewText: String = "",
    val isSubmittingReview: Boolean = false,
    val error: String? = null
)

// Status steps (from PWA statusSteps array)
data class StatusStep(
    val key: String,
    val label: String,
    val emoji: String
)

val ORDER_STATUS_STEPS = listOf(
    StatusStep("pending",    "قيد المراجعة",    "⏰"),
    StatusStep("accepted",   "تم القبول",       "✅"),
    StatusStep("preparing",  "يتم التحضير",     "👨‍🍳"),
    StatusStep("ready",      "جاهز للتوصيل",    "📦"),
    StatusStep("delivering", "في الطريق",       "🚴"),
    StatusStep("delivered",  "تم التوصيل",      "🎉")
)

class OrderTrackingViewModel : ViewModel() {
    private val repo = OrderRepository()
    private val _uiState = MutableStateFlow(TrackingUiState())
    val uiState: StateFlow<TrackingUiState> = _uiState.asStateFlow()

    private var realtimeJob: Job? = null

    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val (order, restaurant) = repo.getOrder(orderId)
                _uiState.update { it.copy(
                    isLoading = false, order = order, restaurant = restaurant
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    // Supabase Realtime — subscribe to order updates
    fun subscribeToOrder(orderId: String) {
        realtimeJob?.cancel()
        realtimeJob = viewModelScope.launch {
            runCatching {
                WasalSupabase.realtime.connect()
                val channel = WasalSupabase.realtime.channel("order-track-$orderId")
                channel.postgresChangeFlow<PostgresAction.Update>(schema = "public") {
                    table = "delivery_orders"
                    filter = "id=eq.$orderId"
                }.collect { change ->
                    val updated = change.decodeRecord<DeliveryOrder>()
                    _uiState.update { it.copy(order = updated) }
                }
                channel.subscribe()
            }
        }
    }

    fun unsubscribeFromOrder(orderId: String) {
        realtimeJob?.cancel()
        viewModelScope.launch {
            runCatching {
                WasalSupabase.realtime.removeChannel(
                    WasalSupabase.realtime.channel("order-track-$orderId")
                )
            }
        }
    }

    fun showReviewDialog() { _uiState.update { it.copy(showReviewDialog = true) } }
    fun dismissReview() { _uiState.update { it.copy(showReviewDialog = false) } }
    fun setRating(r: Int) { _uiState.update { it.copy(reviewRating = r) } }
    fun setReviewText(t: String) { _uiState.update { it.copy(reviewText = t) } }

    suspend fun submitReview(userId: String, restaurantId: String) {
        _uiState.update { it.copy(isSubmittingReview = true) }
        try {
            WasalSupabase.db.from("reviews").insert(mapOf(
                "reviewer_id" to userId,
                "entity_id" to restaurantId,
                "rating" to _uiState.value.reviewRating,
                "review" to _uiState.value.reviewText
            ))
            _uiState.update { it.copy(isSubmittingReview = false, showReviewDialog = false) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isSubmittingReview = false, error = e.message) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        realtimeJob?.cancel()
    }
}
