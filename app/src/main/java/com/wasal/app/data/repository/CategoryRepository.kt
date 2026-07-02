package com.wasal.app.data.repository

import com.wasal.app.data.model.MenuCategory
import com.wasal.app.data.model.MenuItem
import com.wasal.app.data.model.Restaurant
import com.wasal.app.data.remote.WasalSupabase
import io.github.jan-tennert.supabase.postgrest.query.Columns
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CategoryRepository {
    private val db = WasalSupabase.db

    data class CategoryGroup(
        val restaurant: Restaurant,
        val items: List<MenuItem>
    )

    suspend fun getCategoryGroups(categoryName: String): List<CategoryGroup> {
        return try {
            // Step 1: get categories matching name
            val cats = db.from("menu_categories").select(columns = Columns.ALL) {
                filter {
                    eq("name_ar", categoryName)
                    neq("is_active", false)
                }
            }.decodeList<MenuCategory>()
            if (cats.isEmpty()) return emptyList()

            val catIds = cats.map { it.id }
            val restaurantIds = cats.mapNotNull { it.restaurantId }.distinct()
            if (restaurantIds.isEmpty()) return emptyList()

            // Step 2 + 3: parallel fetch items and restaurants
            coroutineScope {
                val itemsJob = async {
                    try {
                        db.from("menu_items").select(columns = Columns.ALL) {
                            filter {
                                isIn("category_id", catIds)
                                neq("is_available", false)
                            }
                        }.decodeList<MenuItem>()
                    } catch (e: Exception) {
                        emptyList<MenuItem>()
                    }
                }

                val restaurantsJob = async {
                    try {
                        db.from("restaurants").select(columns = Columns.ALL) {
                            filter {
                                isIn("id", restaurantIds)
                                eq("is_active", true)
                            }
                        }.decodeList<Restaurant>()
                    } catch (e: Exception) {
                        emptyList<Restaurant>()
                    }
                }

                val items = itemsJob.await()
                val restaurants = restaurantsJob.await()
                if (items.isEmpty() || restaurants.isEmpty()) return@coroutineScope emptyList<CategoryGroup>()

                // Build catId -> restaurantId map
                val catToRestaurant = cats.associate { it.id to it.restaurantId }

                // Group items by restaurantId
                val grouped = mutableMapOf<String, MutableList<MenuItem>>()
                items.forEach { item ->
                    val rId = catToRestaurant[item.categoryId] ?: return@forEach
                    grouped.getOrPut(rId) { mutableListOf() }.add(item)
                }

                restaurants
                    .filter { grouped[it.id]?.isNotEmpty() == true }
                    .map { CategoryGroup(it, (grouped[it.id] ?: emptyList()).sortedBy { item -> item.sortOrder ?: 0 }) }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
