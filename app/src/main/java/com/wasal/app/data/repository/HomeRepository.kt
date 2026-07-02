package com.wasal.app.data.repository

import com.wasal.app.data.remote.WasalSupabase
import com.wasal.app.data.model.DeliveryBanner
import com.wasal.app.data.model.DeliveryOffer
import com.wasal.app.data.model.MenuCategory
import com.wasal.app.data.model.Restaurant
import com.wasal.app.data.model.CustomerAddress
import com.wasal.app.data.model.WasalNotification
import io.github.jan-tennert.supabase.postgrest.query.Columns
import io.github.jan-tennert.supabase.postgrest.query.Order

class HomeRepository {
    private val db = WasalSupabase.db

    // Fetch carousel banners (banner_type = 'carousel', is_active = true, ordered by sort_order)
    suspend fun getBanners(): List<DeliveryBanner> {
        return try {
            db.from("delivery_banners").select(columns = Columns.ALL) {
                filter {
                    eq("banner_type", "carousel")
                    eq("is_active", true)
                }
                order(column = "sort_order", order = Order.ASCENDING)
            }.decodeList<DeliveryBanner>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Fetch active delivery offers (is_active = true, restaurant_id can be null)
    suspend fun getOffers(): List<DeliveryOffer> {
        return try {
            db.from("delivery_company_offers").select(columns = Columns.ALL) {
                filter {
                    eq("is_active", true)
                }
            }.decodeList<DeliveryOffer>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Fetch unique categories (is_active != false, ordered by sort_order, limit 30)
    // Deduplicate by name_ar client-side — keep the one with image_url if conflict
    suspend fun getCategories(): List<MenuCategory> {
        return try {
            val rawCategories = db.from("menu_categories").select(columns = Columns.ALL) {
                filter {
                    neq("is_active", false)
                }
                order(column = "sort_order", order = Order.ASCENDING)
                limit(30L)
            }.decodeList<MenuCategory>()

            // Deduplicate by nameAr client-side: if conflict, keep the one with image_url
            val dedupedMap = mutableMapOf<String, MenuCategory>()
            for (category in rawCategories) {
                val key = category.nameAr
                val existing = dedupedMap[key]
                if (existing == null) {
                    dedupedMap[key] = category
                } else {
                    // if current has an imageUrl and existing doesn't, overwrite
                    if (!category.imageUrl.isNullOrBlank() && existing.imageUrl.isNullOrBlank()) {
                        dedupedMap[key] = category
                    }
                }
            }
            dedupedMap.values.toList().sortedBy { it.sortOrder ?: 0 }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Fetch nearby restaurants filtered by city
    // is_active = true, order by is_featured DESC, rating DESC, limit 10
    // If city is blank/null: return ALL restaurants (no city filter)
    suspend fun getRestaurants(city: String?): List<Restaurant> {
        return try {
            db.from("restaurants").select(columns = Columns.ALL) {
                filter {
                    eq("is_active", true)
                    if (!city.isNullOrBlank()) {
                        eq("city", city)
                    }
                }
                order(column = "is_featured", order = Order.DESCENDING)
                order(column = "rating", order = Order.DESCENDING)
                limit(10L)
            }.decodeList<Restaurant>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Fetch user's best address label
    // Query customer_addresses where customer_id = userId
    // Order: is_default DESC, created_at DESC, limit 5
    // Return: Pair(addressName, cityDistrict)
    suspend fun getUserAddress(userId: String): Pair<String, String> {
        return try {
            val addresses = db.from("customer_addresses").select(columns = Columns.ALL) {
                filter {
                    eq("customer_id", userId)
                }
                order(column = "is_default", order = Order.DESCENDING)
                order(column = "created_at", order = Order.DESCENDING)
                limit(5L)
            }.decodeList<CustomerAddress>()

            if (addresses.isNotEmpty()) {
                val best = addresses.first()
                val cityDistrict = if (!best.district.isNullOrBlank()) {
                    "${best.city}، ${best.district}"
                } else {
                    best.city
                }
                Pair(best.addressName, cityDistrict)
            } else {
                Pair("", "")
            }
        } catch (e: Exception) {
            Pair("", "")
        }
    }

    // Fetch unread notification count for customer
    // notification_type IN ('order_status', 'promotion', 'general', 'promo')
    suspend fun getUnreadCount(userId: String): Int {
        return try {
            val list = db.from("notifications").select(columns = Columns.ALL) {
                filter {
                    eq("user_id", userId)
                    isIn("notification_type", listOf("order_status", "promotion", "general", "promo"))
                }
            }.decodeList<WasalNotification>()
            list.count { it.readAt == null }
        } catch (e: Exception) {
            0
        }
    }
}
