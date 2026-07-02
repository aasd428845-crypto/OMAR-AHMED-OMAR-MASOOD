package com.wasal.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasal.app.data.model.DeliveryBanner
import com.wasal.app.data.model.DeliveryOffer
import com.wasal.app.data.model.MenuCategory
import com.wasal.app.data.model.Restaurant
import com.wasal.app.data.repository.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val banners: List<DeliveryBanner> = emptyList(),
    val offers: List<DeliveryOffer> = emptyList(),
    val categories: List<MenuCategory> = emptyList(),
    val restaurants: List<Restaurant> = emptyList(),
    val addressName: String = "",   // e.g. "المنزل"
    val cityDistrict: String = "",  // e.g. "صنعاء، حدة"
    val unreadCount: Int = 0,
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    private val repo = HomeRepository()
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadAll(userId: String?, city: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Load address info if logged in
                val (addrName, cityDistrict) = if (userId != null) {
                    repo.getUserAddress(userId)
                } else {
                    Pair("", "")
                }

                // Load all data in parallel
                val bannersDeferred = async { runCatching { repo.getBanners() }.getOrDefault(emptyList()) }
                val offersDeferred = async { runCatching { repo.getOffers() }.getOrDefault(emptyList()) }
                val catsDeferred = async { runCatching { repo.getCategories() }.getOrDefault(emptyList()) }
                val restsDeferred = async { runCatching { repo.getRestaurants(city) }.getOrDefault(emptyList()) }
                val unreadDeferred = async {
                    if (userId != null) runCatching { repo.getUnreadCount(userId) }.getOrDefault(0) else 0
                }

                _uiState.update { it.copy(
                    isLoading = false,
                    banners = bannersDeferred.await(),
                    offers = offersDeferred.await(),
                    categories = catsDeferred.await(),
                    restaurants = restsDeferred.await(),
                    addressName = addrName,
                    cityDistrict = cityDistrict,
                    unreadCount = unreadDeferred.await()
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "حدث خطأ أثناء تحميل البيانات") }
            }
        }
    }
}
