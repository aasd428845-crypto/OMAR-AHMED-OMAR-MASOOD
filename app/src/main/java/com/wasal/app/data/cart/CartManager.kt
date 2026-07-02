package com.wasal.app.data.cart

import com.wasal.app.data.model.CartItem
import com.wasal.app.data.model.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

object CartManager {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    private val cartScope = CoroutineScope(Dispatchers.Default)

    val totalPrice: StateFlow<Double> = _items
        .map { list -> list.sumOf { it.menuItem.price * it.quantity } }
        .stateIn(cartScope, SharingStarted.Eagerly, 0.0)

    val itemCount: StateFlow<Int> = _items
        .map { list -> list.sumOf { it.quantity } }
        .stateIn(cartScope, SharingStarted.Eagerly, 0)

    fun addItem(menuItem: MenuItem, restaurantId: String, restaurantName: String) {
        val currentList = _items.value
        val currentRestaurantId = getRestaurantId()

        if (currentRestaurantId != null && currentRestaurantId != restaurantId) {
            // If item from DIFFERENT restaurant: clear cart first, then add
            _items.value = listOf(CartItem(menuItem = menuItem, quantity = 1, restaurantId = restaurantId, restaurantName = restaurantName))
        } else {
            // If same restaurant: increment quantity if exists, else add new
            val existingIndex = currentList.indexOfFirst { it.menuItem.id == menuItem.id }
            if (existingIndex != -1) {
                val updatedList = currentList.mapIndexed { index, item ->
                    if (index == existingIndex) {
                        item.copy(quantity = item.quantity + 1)
                    } else {
                        item
                    }
                }
                _items.value = updatedList
            } else {
                _items.value = currentList + CartItem(menuItem = menuItem, quantity = 1, restaurantId = restaurantId, restaurantName = restaurantName)
            }
        }
    }

    fun incrementItem(menuItemId: String) {
        val currentList = _items.value
        _items.value = currentList.map { item ->
            if (item.menuItem.id == menuItemId) {
                item.copy(quantity = item.quantity + 1)
            } else {
                item
            }
        }
    }

    fun decrementItem(menuItemId: String) {
        val currentList = _items.value
        val updatedList = currentList.mapNotNull { item ->
            if (item.menuItem.id == menuItemId) {
                if (item.quantity > 1) {
                    item.copy(quantity = item.quantity - 1)
                } else {
                    null // removes if quantity reaches 0
                }
            } else {
                item
            }
        }
        _items.value = updatedList
    }

    fun removeItem(menuItemId: String) {
        _items.value = _items.value.filter { it.menuItem.id != menuItemId }
    }

    fun clearCart() {
        _items.value = emptyList()
    }

    fun getRestaurantId(): String? {
        return _items.value.firstOrNull()?.restaurantId
    }
}
