package com.wasal.app.data.repository

import com.wasal.app.data.model.CustomerAddress
import com.wasal.app.data.model.ActiveOffer
import com.wasal.app.data.model.Restaurant
import com.wasal.app.data.model.CartItem
import com.wasal.app.data.remote.WasalSupabase
import com.wasal.app.data.cart.CartManager
import io.github.jan-tennert.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DBOrderItem(
    val id: String,
    @SerialName("name_ar") val nameAr: String,
    val price: Double,
    val quantity: Int,
    @SerialName("image_url") val imageUrl: String? = null
)

@Serializable
data class DBCart(
    @SerialName("customer_id") val customerId: String,
    @SerialName("restaurant_id") val restaurantId: String,
    val items: List<DBOrderItem>,
    @SerialName("total_amount") val totalAmount: Double
)

@Serializable
data class DBOrder(
    @SerialName("customer_id") val customerId: String,
    @SerialName("restaurant_id") val restaurantId: String,
    @SerialName("delivery_company_id") val deliveryCompanyId: String? = null,
    @SerialName("customer_name") val customerName: String,
    @SerialName("customer_phone") val customerPhone: String,
    @SerialName("customer_address") val customerAddress: String,
    @SerialName("delivery_lat") val deliveryLat: Double? = null,
    @SerialName("delivery_lng") val deliveryLng: Double? = null,
    val items: List<DBOrderItem>,
    val subtotal: Double,
    @SerialName("delivery_fee") val deliveryFee: Double,
    val tax: Double = 0.0,
    val total: Double,
    @SerialName("payment_method") val paymentMethod: String = "pending",
    val status: String = "pending",
    val notes: String? = null,
    @SerialName("applied_offer_id") val appliedOfferId: String? = null,
    @SerialName("applied_offer_type") val appliedOfferType: String? = null,
    @SerialName("applied_offer_title") val appliedOfferTitle: String? = null
)

@Serializable
data class OrderInsertResult(
    val id: String
)

class CartRepository {
    private val db = WasalSupabase.db

    // Fetch restaurant info for cart items
    // Returns map of restaurantId -> Restaurant
    suspend fun getRestaurantsForCart(restaurantIds: List<String>): Map<String, Restaurant> {
        if (restaurantIds.isEmpty()) return emptyMap()
        val list = db.from("restaurants")
            .select {
                filter { isIn("id", restaurantIds) }
            }
            .decodeList<Restaurant>()
        return list.associateBy { it.id }
    }

    // Fetch best active delivery offer for a delivery company
    // Returns: restaurant-specific offer first, then company-wide, or null
    suspend fun getActiveOffer(
        deliveryCompanyId: String,
        restaurantId: String
    ): ActiveOffer? {
        val offers = db.from("delivery_company_offers")
            .select {
                filter {
                    eq("delivery_company_id", deliveryCompanyId)
                    eq("is_active", true)
                }
            }
            .decodeList<ActiveOffer>()
        return offers.find { it.restaurantId == restaurantId }
            ?: offers.find { it.restaurantId == null || it.restaurantId.isBlank() }
    }

    // Sync CartManager state to Supabase carts table
    // Called before proceeding to checkout
    suspend fun syncCartToSupabase(
        customerId: String,
        restaurantId: String,
        items: List<CartItem>,
        total: Double
    ) {
        val dbCart = DBCart(
            customerId = customerId,
            restaurantId = restaurantId,
            items = items.map { ci ->
                DBOrderItem(
                    id = ci.menuItem.id,
                    nameAr = ci.menuItem.nameAr,
                    price = ci.menuItem.effectivePrice,
                    quantity = ci.quantity,
                    imageUrl = ci.menuItem.imageUrl
                )
            },
            totalAmount = total
        )

        // Upsert to carts table
        db.from("carts").upsert(dbCart) {
            onConflict = "customer_id,restaurant_id"
        }
    }

    // Load customer addresses
    suspend fun getAddresses(customerId: String): List<CustomerAddress> {
        return db.from("customer_addresses")
            .select {
                filter { eq("customer_id", customerId) }
            }
            .decodeList<CustomerAddress>()
            .sortedByDescending { it.isDefault == true }
    }

    // Place order -> insert into delivery_orders
    suspend fun placeOrder(
        customerId: String,
        restaurantId: String,
        deliveryCompanyId: String?,
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        deliveryLat: Double?,
        deliveryLng: Double?,
        items: List<CartItem>,
        subtotal: Double,
        deliveryFee: Double,
        total: Double,
        notes: String,
        appliedOffer: ActiveOffer?
    ): String { // returns orderId
        val orderItems = items.map { ci ->
            DBOrderItem(
                id = ci.menuItem.id,
                nameAr = ci.menuItem.nameAr,
                price = ci.menuItem.effectivePrice,
                quantity = ci.quantity,
                imageUrl = ci.menuItem.imageUrl
            )
        }

        val dbOrder = DBOrder(
            customerId = customerId,
            restaurantId = restaurantId,
            deliveryCompanyId = deliveryCompanyId,
            customerName = customerName,
            customerPhone = customerPhone,
            customerAddress = customerAddress,
            deliveryLat = deliveryLat,
            deliveryLng = deliveryLng,
            items = orderItems,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            total = total,
            notes = notes.takeIf { it.isNotBlank() },
            appliedOfferId = appliedOffer?.id,
            appliedOfferType = appliedOffer?.offerType,
            appliedOfferTitle = appliedOffer?.title
        )

        val result = db.from("delivery_orders")
            .insert(dbOrder)
            .decodeSingle<OrderInsertResult>()

        // Clear cart after successful order
        try {
            db.from("carts").delete {
                filter {
                    eq("customer_id", customerId)
                    eq("restaurant_id", restaurantId)
                }
            }
        } catch (e: Exception) {
            // Ignore minor remote cart cleanup errors
        }
        CartManager.clearCart()

        return result.id
    }
}
