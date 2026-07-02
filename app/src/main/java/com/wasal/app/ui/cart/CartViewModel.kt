package com.wasal.app.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasal.app.data.cart.CartManager
import com.wasal.app.data.model.ActiveOffer
import com.wasal.app.data.model.CartItem
import com.wasal.app.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RestaurantCartGroup(
    val restaurantId: String,
    val restaurantName: String,
    val logoUrl: String?,
    val deliveryFee: Double,
    val minOrderAmount: Double,
    val deliveryCompanyId: String?,
    val items: List<CartItem>,
    val activeOffer: ActiveOffer?,
    val subtotal: Double,
    val offerApplies: Boolean,
    val computedDeliveryFee: Double,
    val computedSubtotal: Double,
    val total: Double
)

data class CartUiState(
    val isLoading: Boolean = true,
    val groups: List<RestaurantCartGroup> = emptyList(),
    val grandTotal: Double = 0.0,
    val error: String? = null
)

class CartViewModel : ViewModel() {
    private val repo = CartRepository()
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun loadCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val cartItems = CartManager.items.value
                if (cartItems.isEmpty()) {
                    _uiState.update { it.copy(isLoading = false, groups = emptyList()) }
                    return@launch
                }

                // Group by restaurant
                val grouped = cartItems.groupBy { it.restaurantId }
                val restaurantIds = grouped.keys.toList()

                // Fetch restaurant info
                val restaurants = repo.getRestaurantsForCart(restaurantIds)

                // Build groups with pricing
                val groups = grouped.map { (rId, items) ->
                    val r = restaurants[rId]
                    val subtotal = items.sumOf { it.menuItem.effectivePrice * it.quantity }
                    val fee = r?.deliveryFee ?: 0.0
                    val companyId = r?.deliveryCompanyId
                    val offer = if (companyId != null)
                        runCatching { repo.getActiveOffer(companyId, rId) }.getOrNull()
                    else null

                    val minForOffer = offer?.minOrderAmount ?: 0.0
                    val offerApplies = offer != null && subtotal >= minForOffer
                    val computedFee = if (offerApplies) offer!!.computedDeliveryFee(fee) else fee
                    val computedSub = if (offerApplies) offer!!.computedSubtotal(subtotal) else subtotal

                    RestaurantCartGroup(
                        restaurantId = rId,
                        restaurantName = r?.nameAr ?: items.first().restaurantName,
                        logoUrl = r?.logoUrl,
                        deliveryFee = fee,
                        minOrderAmount = r?.minOrderAmount ?: 0.0,
                        deliveryCompanyId = companyId,
                        items = items,
                        activeOffer = offer,
                        subtotal = subtotal,
                        offerApplies = offerApplies,
                        computedDeliveryFee = computedFee,
                        computedSubtotal = computedSub,
                        total = computedSub + computedFee
                    )
                }

                _uiState.update { it.copy(
                    isLoading = false,
                    groups = groups,
                    grandTotal = groups.sumOf { it.total }
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun incrementItem(menuItemId: String) {
        CartManager.incrementItem(menuItemId)
        loadCart()
    }

    fun decrementItem(menuItemId: String) {
        CartManager.decrementItem(menuItemId)
        loadCart()
    }

    // Observe CartManager changes
    init {
        viewModelScope.launch {
            CartManager.items.collect { loadCart() }
        }
    }
}
