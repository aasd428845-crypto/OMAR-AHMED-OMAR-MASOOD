package com.wasal.app.ui.restaurant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasal.app.data.cart.CartManager
import com.wasal.app.data.model.MenuCategory
import com.wasal.app.data.model.MenuItem
import com.wasal.app.data.model.Restaurant
import com.wasal.app.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RestaurantMenuUiState(
    val isLoading: Boolean = true,
    val restaurant: Restaurant? = null,
    val categories: List<MenuCategory> = emptyList(),
    val items: List<MenuItem> = emptyList(),
    val filteredItems: List<MenuItem> = emptyList(),
    val searchQuery: String = "",
    val activeCategoryId: String? = null,
    val cartItemCount: Int = 0,
    val cartTotal: Double = 0.0,
    val error: String? = null
)

class RestaurantMenuViewModel : ViewModel() {
    private val repo = RestaurantRepository()
    private val _uiState = MutableStateFlow(RestaurantMenuUiState())
    val uiState: StateFlow<RestaurantMenuUiState> = _uiState.asStateFlow()

    init {
        // Observe cart changes
        viewModelScope.launch {
            CartManager.items.collect { cartItems ->
                _uiState.update { it.copy(
                    cartItemCount = cartItems.sumOf { c -> c.quantity },
                    cartTotal = cartItems.sumOf { c -> c.menuItem.effectivePrice * c.quantity }
                )}
            }
        }
    }

    fun loadMenu(restaurantId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val menu = repo.getRestaurantMenu(restaurantId)
                _uiState.update { it.copy(
                    isLoading = false,
                    restaurant = menu.restaurant,
                    categories = menu.categories,
                    items = menu.items,
                    filteredItems = menu.items,
                    activeCategoryId = menu.categories.firstOrNull()?.id,
                    error = null
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "حدث خطأ أثناء تحميل البيانات") }
            }
        }
    }

    fun onSearchChanged(query: String) {
        val all = _uiState.value.items
        val filtered = if (query.isBlank()) {
            all
        } else {
            all.filter {
                it.nameAr.contains(query, ignoreCase = true) || it.description?.contains(query, ignoreCase = true) == true
            }
        }
        _uiState.update { it.copy(searchQuery = query, filteredItems = filtered) }
    }

    fun setActiveCategory(catId: String) {
        _uiState.update { it.copy(activeCategoryId = catId) }
    }

    fun addToCart(item: MenuItem, restaurantId: String, restaurantName: String) {
        CartManager.addItem(item, restaurantId, restaurantName)
    }
}
