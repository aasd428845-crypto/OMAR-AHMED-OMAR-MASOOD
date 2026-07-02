package com.wasal.app.data.repository

import com.wasal.app.data.model.DeliveryOrder
import com.wasal.app.data.model.Restaurant
import com.wasal.app.data.model.AppNotification
import com.wasal.app.data.remote.WasalSupabase

class OrderRepository {
    private val db = WasalSupabase.db

    // Fetch single order + restaurant info
    suspend fun getOrder(orderId: String): Pair<DeliveryOrder, Restaurant?> {
        val order = db.from("delivery_orders")
            .select { filter { eq("id", orderId) } }
            .decodeSingle<DeliveryOrder>()
        val restaurant = if (order.restaurantId != null) {
            runCatching {
                db.from("restaurants")
                    .select {
                        filter { eq("id", order.restaurantId) }
                    }
                    .decodeSingle<Restaurant>()
            }.getOrNull()
        } else null
        return Pair(order, restaurant)
    }

    // Fetch order history for customer
    suspend fun getCustomerOrders(customerId: String): List<DeliveryOrder> {
        return db.from("delivery_orders")
            .select { filter { eq("customer_id", customerId) } }
            .decodeList<DeliveryOrder>()
            // Sort by created_at descending client-side
            .sortedByDescending { it.createdAt }
    }

    // Cancel order
    suspend fun cancelOrder(orderId: String) {
        db.from("delivery_orders")
            .update(mapOf("status" to "cancelled")) {
                filter { eq("id", orderId) }
            }
    }

    // Fetch notifications
    suspend fun getNotifications(userId: String): List<AppNotification> {
        return db.from("notifications")
            .select { filter { eq("user_id", userId) } }
            .decodeList<AppNotification>()
            .sortedByDescending { it.createdAt }
            .take(80)
    }

    // Mark single notification as read
    suspend fun markNotificationRead(notifId: String) {
        val now = java.time.Instant.now().toString()
        db.from("notifications")
            .update(mapOf("read_at" to now)) {
                filter { eq("id", notifId) }
            }
    }

    // Mark all notifications as read for user
    suspend fun markAllNotificationsRead(userId: String) {
        val now = java.time.Instant.now().toString()
        db.from("notifications")
            .update(mapOf("read_at" to now)) {
                filter {
                    eq("user_id", userId)
                    isNull("read_at")
                }
            }
    }
}
