package com.wasal.app.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasal.app.data.cart.CartManager
import com.wasal.app.data.model.ActiveOffer
import com.wasal.app.data.model.CartItem
import com.wasal.app.data.model.CustomerAddress
import com.wasal.app.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class CheckoutState {
    object Idle : CheckoutState()
    object LoadingData : CheckoutState()
    object ReadyToOrder : CheckoutState()
    object PlacingOrder : CheckoutState()
    data class OrderPlaced(val orderId: String) : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}

data class CheckoutUiState(
    val state: CheckoutState = CheckoutState.LoadingData,
    val restaurantName: String = "",
    val restaurantId: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val addresses: List<CustomerAddress> = emptyList(),
    val selectedAddress: CustomerAddress? = null,
    val activeOffer: ActiveOffer? = null,
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val total: Double = 0.0,
    val notes: String = "",
    val phone: String = ""
)

class CheckoutViewModel : ViewModel() {
    private val repo = CartRepository()
    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    // deliveryCompanyId stored separately (not exposed to UI directly)
    private var deliveryCompanyId: String? = null
    private var baseFee: Double = 0.0

    fun loadCheckout(restaurantId: String, userId: String, userPhone: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(state = CheckoutState.LoadingData) }
            try {
                val cartItems = CartManager.items.value.filter { it.restaurantId == restaurantId }
                val subtotal = cartItems.sumOf { it.menuItem.effectivePrice * it.quantity }

                // Fetch restaurant + addresses in parallel
                val restaurantMap = repo.getRestaurantsForCart(listOf(restaurantId))
                val restaurant = restaurantMap[restaurantId]
                val addresses = repo.getAddresses(userId)

                deliveryCompanyId = restaurant?.deliveryCompanyId
                baseFee = restaurant?.deliveryFee ?: 0.0

                val offer = if (deliveryCompanyId != null)
                    runCatching { repo.getActiveOffer(deliveryCompanyId!!, restaurantId) }.getOrNull()
                else null

                val defaultAddress = addresses.find { it.isDefault == true } ?: addresses.firstOrNull()

                _uiState.update { it.copy(
                    state = CheckoutState.ReadyToOrder,
                    restaurantName = restaurant?.nameAr ?: "",
                    restaurantId = restaurantId,
                    cartItems = cartItems,
                    addresses = addresses,
                    selectedAddress = defaultAddress,
                    activeOffer = offer,
                    subtotal = subtotal,
                    phone = userPhone,
                    deliveryFee = computeFee(offer, subtotal, baseFee),
                    total = subtotal + computeFee(offer, subtotal, baseFee)
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(state = CheckoutState.Error(e.message ?: "حدث خطأ أثناء تحميل بيانات الطلب")) }
            }
        }
    }

    fun selectAddress(address: CustomerAddress) {
        val subtotal = _uiState.value.subtotal
        val offer = _uiState.value.activeOffer
        val fee = computeFee(offer, subtotal, baseFee)
        _uiState.update { it.copy(
            selectedAddress = address,
            deliveryFee = fee,
            total = subtotal + fee
        )}
    }

    fun updateNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun updatePhone(phone: String) {
        _uiState.update { it.copy(phone = phone) }
    }

    fun placeOrder(userId: String, userFullName: String) {
        val s = _uiState.value
        val address = s.selectedAddress ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(state = CheckoutState.PlacingOrder) }
            try {
                val fullAddr = listOfNotNull(
                    address.fullAddress, address.district,
                    address.street, address.landmark
                ).joinToString("، ")

                val orderId = repo.placeOrder(
                    customerId = userId,
                    restaurantId = s.restaurantId,
                    deliveryCompanyId = deliveryCompanyId,
                    customerName = address.customerName ?: userFullName,
                    customerPhone = address.phone ?: s.phone,
                    customerAddress = fullAddr,
                    deliveryLat = address.latitude,
                    deliveryLng = address.longitude,
                    items = s.cartItems,
                    subtotal = s.subtotal,
                    deliveryFee = s.deliveryFee,
                    total = s.total,
                    notes = s.notes,
                    appliedOffer = if (isOfferApplicable(s.activeOffer, s.subtotal))
                        s.activeOffer else null
                )
                _uiState.update { it.copy(state = CheckoutState.OrderPlaced(orderId)) }
            } catch (e: Exception) {
                _uiState.update { it.copy(state = CheckoutState.Error(e.message ?: "فشل في إتمام الطلب")) }
            }
        }
    }

    private fun computeFee(offer: ActiveOffer?, subtotal: Double, baseFee: Double): Double {
        if (!isOfferApplicable(offer, subtotal)) return baseFee
        return offer!!.computedDeliveryFee(baseFee)
    }

    private fun isOfferApplicable(offer: ActiveOffer?, subtotal: Double): Boolean {
        if (offer == null) return false
        val minOrder = offer.minOrderAmount ?: 0.0
        return subtotal >= minOrder
    }
}
