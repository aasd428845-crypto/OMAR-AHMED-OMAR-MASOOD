package com.wasal.app.data.repository

import com.wasal.app.data.model.MenuCategory
import com.wasal.app.data.model.MenuItem
import com.wasal.app.data.model.Restaurant
import com.wasal.app.data.remote.WasalSupabase
import io.github.jan-tennert.supabase.postgrest.query.Columns
import io.github.jan-tennert.supabase.postgrest.query.Order
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class RestaurantMenu(
    val restaurant: Restaurant,
    val categories: List<MenuCategory>,
    val items: List<MenuItem>
)

class RestaurantRepository {
    private val db = WasalSupabase.db

    suspend fun getRestaurantMenu(restaurantId: String): RestaurantMenu {
        return coroutineScope {
            val restaurantDeferred = async {
                db.from("restaurants").select(columns = Columns.ALL) {
                    filter {
                        eq("id", restaurantId)
                    }
                }.decodeSingle<Restaurant>()
            }

            val categoriesDeferred = async {
                db.from("menu_categories").select(columns = Columns.ALL) {
                    filter {
                        eq("restaurant_id", restaurantId)
                        neq("is_active", false)
                    }
                    order(column = "sort_order", order = Order.ASCENDING)
                }.decodeList<MenuCategory>()
            }

            val itemsDeferred = async {
                db.from("menu_items").select(columns = Columns.ALL) {
                    filter {
                        eq("restaurant_id", restaurantId)
                        neq("is_available", false)
                    }
                    order(column = "sort_order", order = Order.ASCENDING)
                }.decodeList<MenuItem>()
            }

            RestaurantMenu(
                restaurant = restaurantDeferred.await(),
                categories = categoriesDeferred.await(),
                items = itemsDeferred.await()
            )
        }
    }
}
