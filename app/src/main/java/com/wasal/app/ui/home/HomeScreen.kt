package com.wasal.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wasal.app.ui.home.components.*
import com.wasal.app.ui.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    userId: String?,       // null if guest
    userCity: String?,     // from profile
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId, userCity) {
        viewModel.loadAll(userId, userCity)
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                WasalHomeTopBar(
                    addressName = uiState.addressName,
                    cityDistrict = uiState.cityDistrict,
                    unreadCount = uiState.unreadCount,
                    onNotificationClick = { navController.navigate(Screen.Notifications.route) }
                )
            },
            bottomBar = {
                WasalBottomNav(navController = navController, currentRoute = Screen.Home.route)
            }
        ) { padding ->
            if (uiState.isLoading) {
                HomeLoadingSkeleton(Modifier.padding(padding))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF8F9FA))
                        .padding(padding),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {

                    // ─── Section: Banner Carousel ───────────────────────────
                    item {
                        BannerCarousel(
                            banners = uiState.banners,
                            onBannerClick = { linkTab ->
                                when (linkTab) {
                                    "restaurants" -> navController.navigate(Screen.Restaurants.route)
                                    "shipments"   -> navController.navigate(Screen.DeliveryRequest.route)
                                    else -> navController.navigate(Screen.Restaurants.route)
                                }
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    // ─── Section: Offers ────────────────────────────────────
                    if (uiState.offers.isNotEmpty()) {
                        item {
                            OffersRow(
                                offers = uiState.offers,
                                onOfferClick = { offer ->
                                    if (offer.restaurantId != null) {
                                        navController.navigate(Screen.RestaurantMenu.createRoute(offer.restaurantId))
                                    } else {
                                        navController.navigate(Screen.Restaurants.route)
                                    }
                                }
                            )
                        }
                    }

                    // ─── Section: Categories ────────────────────────────────
                    if (uiState.categories.isNotEmpty()) {
                        item {
                            CategoryScrollerSection(
                                categories = uiState.categories,
                                onCategoryClick = { cat ->
                                    navController.navigate(Screen.Category.createRoute(cat.nameAr))
                                },
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    // ─── Section: Nearby Restaurants ───────────────────────
                    if (uiState.restaurants.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "مطاعم بالقرب منك",
                                onMoreClick = { navController.navigate(Screen.Restaurants.route) },
                                modifier = Modifier.padding(horizontal = 16.dp, top = 8.dp)
                            )
                        }
                        items(uiState.restaurants, key = { it.id }) { restaurant ->
                            RestaurantCard(
                                restaurant = restaurant,
                                onClick = { navController.navigate(Screen.RestaurantMenu.createRoute(restaurant.id)) },
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onMoreClick) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "عرض الكل",
                    color = Color(0xFF1A5C3A),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color(0xFF1A5C3A),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black
        )
    }
}
