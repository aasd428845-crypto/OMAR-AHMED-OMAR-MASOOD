package com.wasal.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.double

@Serializable
data class UserProfile(
    @SerialName("user_id") val userId: String,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("account_status") val accountStatus: String? = null
)

@Serializable
data class Restaurant(
    val id: String,
    @SerialName("name_ar") val nameAr: String,
    val description: String? = null,
    @SerialName("cover_image") val coverImage: String? = null,
    @SerialName("logo_url") val logoUrl: String? = null,
    val rating: Double? = null,
    @SerialName("total_ratings") val totalRatings: Int? = null,
    @SerialName("estimated_delivery_time") val estimatedDeliveryTime: Int? = null,
    @SerialName("delivery_fee") val deliveryFee: Double? = null,
    @SerialName("min_order_amount") val minOrderAmount: Double? = null,
    @SerialName("is_active") val isActive: Boolean? = null,
    @SerialName("is_featured") val isFeatured: Boolean? = null,
    @SerialName("delivery_company_id") val deliveryCompanyId: String? = null,
    val city: String? = null,
    val address: String? = null,
    @SerialName("opening_time") val openingTime: String? = null,
    @SerialName("closing_time") val closingTime: String? = null,
    @SerialName("location_lat") val locationLat: Double? = null,
    @SerialName("location_lng") val locationLng: Double? = null
)

@Serializable
data class MenuCategory(
    val id: String,
    @SerialName("name_ar") val nameAr: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("restaurant_id") val restaurantId: String? = null,
    @SerialName("is_active") val isActive: Boolean? = null,
    @SerialName("sort_order") val sortOrder: Int? = null
)

@Serializable
data class MenuItem(
    val id: String,
    @SerialName("name_ar") val nameAr: String,
    val description: String? = null,
    val price: Double,
    @SerialName("original_price") val originalPrice: Double? = null,
    @SerialName("discounted_price") val discountedPrice: Double? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("category_id") val categoryId: String? = null,
    @SerialName("restaurant_id") val restaurantId: String? = null,
    @SerialName("is_available") val isAvailable: Boolean? = null,
    @SerialName("is_featured") val isFeatured: Boolean? = null,
    @SerialName("is_popular") val isPopular: Boolean? = null,
    @SerialName("sort_order") val sortOrder: Int? = null,
    @SerialName("preparation_time") val preparationTime: Int? = null,
    val calories: Int? = null
) {
    // Computed: effective price after discount
    val effectivePrice: Double get() =
        if (discountedPrice != null && discountedPrice < price) discountedPrice else price

    val hasDiscount: Boolean get() =
        discountedPrice != null && discountedPrice < price

    val discountPercent: Int get() =
        if (hasDiscount) ((1.0 - effectivePrice / price) * 100).toInt() else 0
}

@Serializable
data class DeliveryBanner(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("badge_text") val badgeText: String? = null,
    @SerialName("restaurant_id") val restaurantId: String? = null,
    @SerialName("is_active") val isActive: Boolean? = null,
    @SerialName("link_tab") val linkTab: String? = null,
    @SerialName("banner_type") val bannerType: String? = null,
    @SerialName("sort_order") val sortOrder: Int? = null
)

@Serializable
data class DeliveryOffer(
    val id: String,
    val title: String,
    @SerialName("offer_type") val offerType: String,
    @SerialName("badge_text") val badgeText: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("restaurant_id") val restaurantId: String? = null,
    @SerialName("is_active") val isActive: Boolean? = null,
    @SerialName("discount_percent") val discountPercent: Int? = null
)

@Serializable
data class CustomerAddress(
    val id: String,
    @SerialName("customer_id") val customerId: String,
    @SerialName("address_name") val addressName: String,
    @SerialName("full_address") val fullAddress: String? = null,
    val city: String? = null,
    val district: String? = null,
    val street: String? = null,
    val landmark: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("is_default") val isDefault: Boolean? = null,
    val phone: String? = null,
    @SerialName("customer_name") val customerName: String? = null
) {
    val displayLabel: String get() =
        listOfNotNull(addressName, city, district).joinToString("، ")
}

@Serializable
data class ActiveOffer(
    val id: String,
    val title: String,
    @SerialName("offer_type") val offerType: String,
    @SerialName("min_order_amount") val minOrderAmount: Double? = null,
    @SerialName("discount_percent") val discountPercent: Double? = null,
    @SerialName("discount_amount") val discountAmount: Double? = null,
    @SerialName("restaurant_id") val restaurantId: String? = null,
    @SerialName("delivery_company_id") val deliveryCompanyId: String? = null
) {
    fun computedDeliveryFee(baseFee: Double): Double = when (offerType) {
        "free_delivery" -> 0.0
        "percent_off_delivery" -> baseFee * (1.0 - (discountPercent ?: 0.0) / 100.0)
        "fixed_off_delivery" -> (baseFee - (discountAmount ?: 0.0)).coerceAtLeast(0.0)
        else -> baseFee
    }
    fun computedSubtotal(subtotal: Double): Double = when (offerType) {
        "percent_off_order" -> subtotal * (1.0 - (discountPercent ?: 0.0) / 100.0)
        "fixed_off_order" -> (subtotal - (discountAmount ?: 0.0)).coerceAtLeast(0.0)
        else -> subtotal
    }
}

@Serializable
data class WasalNotification(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("read_at") val readAt: String? = null,
    @SerialName("notification_type") val notificationType: String
)

data class CartItem(
    val menuItem: MenuItem,
    var quantity: Int = 1,
    val restaurantId: String,
    val restaurantName: String
)

@Serializable
data class DeliveryOrder(
  val id: String,
  val status: String? = null,
  @SerialName("customer_id") val customerId: String? = null,
  @SerialName("restaurant_id") val restaurantId: String? = null,
  @SerialName("rider_id") val riderId: String? = null,
  val items: JsonArray? = null,
  val subtotal: Double = 0.0,
  @SerialName("delivery_fee") val deliveryFee: Double = 0.0,
  val total: Double = 0.0,
  @SerialName("customer_name") val customerName: String = "",
  @SerialName("customer_phone") val customerPhone: String = "",
  @SerialName("customer_address") val customerAddress: String = "",
  @SerialName("payment_method") val paymentMethod: String? = null,
  @SerialName("payment_status") val paymentStatus: String? = null,
  val notes: String? = null,
  @SerialName("created_at") val createdAt: String? = null,
  @SerialName("delivered_at") val deliveredAt: String? = null,
  @SerialName("order_type") val orderType: String? = null,
  @SerialName("negotiation_status") val negotiationStatus: String? = null,
  @SerialName("customer_accepted") val customerAccepted: Boolean? = null,
  @SerialName("proposed_price") val proposedPrice: Double? = null,
  @SerialName("applied_offer_id") val appliedOfferId: String? = null,
  @SerialName("applied_offer_title") val appliedOfferTitle: String? = null
) {
  val statusLabel: String get() = when (status) {
    "pending"    -> "قيد المراجعة"
    "accepted"   -> "تم القبول"
    "confirmed"  -> "مؤكد"
    "preparing"  -> "يتم التحضير"
    "ready"      -> "جاهز للتوصيل"
    "delivering" -> "في الطريق"
    "on_the_way" -> "في الطريق"
    "delivered"  -> "تم التوصيل"
    "cancelled"  -> "ملغى"
    else         -> status ?: "غير معروف"
  }
  val statusColor: Color get() = when (status) {
    "pending"            -> Color(0xFFF59E0B)
    "accepted","confirmed" -> Color(0xFF3B82F6)
    "preparing"          -> Color(0xFFF97316)
    "ready"              -> Color(0xFF8B5CF6)
    "delivering","on_the_way" -> Color(0xFF1A5C3A)
    "delivered"          -> Color(0xFF22C55E)
    "cancelled"          -> Color(0xFFEF4444)
    else                 -> Color(0xFF6B7280)
  }
  val isCancellable: Boolean get() =
    status in listOf("pending", "confirmed")
  val isCompleted: Boolean get() =
    status == "delivered"
  val isCancelled: Boolean get() =
    status == "cancelled"
}

@Serializable
data class AppNotification(
  val id: String,
  @SerialName("user_id") val userId: String,
  val title: String,
  val body: String? = null,
  @SerialName("notification_type") val notificationType: String? = null,
  val data: JsonObject? = null,
  @SerialName("read_at") val readAt: String? = null,
  @SerialName("created_at") val createdAt: String? = null
) {
  val isUnread: Boolean get() = readAt == null
  val emoji: String get() = when {
    notificationType == null -> "🔔"
    notificationType.contains("order") || notificationType.contains("assign") -> "📦"
    notificationType.contains("payment") -> "💳"
    notificationType.contains("promo") -> "🎁"
    notificationType.contains("delivery") -> "🚚"
    notificationType.contains("cash") -> "💰"
    else -> "🔔"
  }
  fun timeAgo(): String {
    val created = createdAt ?: return ""
    return try {
      val cleanDate = created.substringBefore(".")
      val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
      sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
      val time = sdf.parse(cleanDate)?.time ?: return ""
      val diffMs = System.currentTimeMillis() - time
      val mins = (diffMs / 60000).toInt()
      if (mins < 1) return "الآن"
      if (mins < 60) return "منذ $mins د"
      val hrs = mins / 60
      if (hrs < 24) return "منذ $hrs س"
      return "منذ ${hrs / 24} أيام"
    } catch (e: Exception) {
      ""
    }
  }
}
