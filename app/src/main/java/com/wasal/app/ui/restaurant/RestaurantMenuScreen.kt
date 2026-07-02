package com.wasal.app.ui.restaurant

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wasal.app.data.cart.CartManager
import com.wasal.app.data.model.MenuCategory
import com.wasal.app.data.model.MenuItem
import com.wasal.app.ui.home.components.getCategoryImage
import com.wasal.app.ui.navigation.Screen
import com.wasal.app.ui.restaurant.components.RestaurantMenuItemCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RestaurantMenuScreen(
    navController: NavController,
    restaurantId: String,
    viewModel: RestaurantMenuViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Map categoryId -> starting index in LazyColumn for scroll-to-section
    val categoryIndexMap = remember(uiState.categories, uiState.filteredItems) {
        buildCategoryIndexMap(uiState.categories, uiState.filteredItems)
    }

    LaunchedEffect(restaurantId) {
        viewModel.loadMenu(restaurantId)
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF1A5C3A))
                }
            } else if (uiState.restaurant != null) {
                val restaurant = uiState.restaurant!!

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF8F9FA)),
                    contentPadding = PaddingValues(
                        bottom = if (uiState.cartItemCount > 0) 100.dp else 32.dp
                    )
                ) {

                    // ─── COVER IMAGE ──────────────────────────────────────────
                    item {
                        Box(Modifier.fillMaxWidth()) {
                            // Cover photo
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(176.dp)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF1A5C3A).copy(alpha = 0.3f), Color(0xFFF5F5F5))
                                        )
                                    )
                            ) {
                                val coverUrl = restaurant.coverImage
                                if (coverUrl != null) {
                                    AsyncImage(
                                        model = coverUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Text(
                                        "🍽️",
                                        modifier = Modifier.align(Alignment.Center),
                                        fontSize = 64.sp
                                    )
                                }
                            }

                            // Back button (top end in RTL = top right visually)
                            IconButton(
                                onClick = { navController.navigateUp() },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(36.dp)
                                    .background(Color(0x66000000), CircleShape)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward, null,
                                    tint = Color.White
                                )
                            }

                            // ─── LOGO (overlapping cover, -32dp offset) ──────────
                            Box(
                                Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 16.dp)
                                    .offset(y = 40.dp)
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White)
                                    .border(4.dp, Color.White, RoundedCornerShape(16.dp))
                            ) {
                                val logoUrl = restaurant.logoUrl
                                if (logoUrl != null) {
                                    AsyncImage(
                                        model = logoUrl, contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Text(
                                        "🍽️",
                                        modifier = Modifier.align(Alignment.Center),
                                        fontSize = 32.sp
                                    )
                                }
                            }
                        }
                    }

                    // ─── RESTAURANT INFO ──────────────────────────────────────
                    item {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(top = 44.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
                        ) {
                            // Name
                            Text(
                                text = restaurant.nameAr,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Description
                            if (!restaurant.description.isNullOrBlank()) {
                                Text(
                                    text = restaurant.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF757575),
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            // Stats row (rating + time + fee + min order)
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val fee = restaurant.deliveryFee ?: 0.0
                                // Delivery fee
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(
                                        if (fee == 0.0) "مجاني" else "${fee.toLong()} ر.ي",
                                        fontSize = 11.sp,
                                        color = if (fee == 0.0) Color(0xFF52B788) else Color(0xFF757575),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        Icons.Default.LocalShipping, null,
                                        modifier = Modifier.size(13.dp), tint = Color(0xFF757575)
                                    )
                                }
                                // Delivery time
                                if (restaurant.estimatedDeliveryTime != null) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Text(
                                            "${restaurant.estimatedDeliveryTime} د",
                                            fontSize = 11.sp, color = Color(0xFF757575)
                                        )
                                        Icon(
                                            Icons.Default.AccessTime, null,
                                            modifier = Modifier.size(13.dp), tint = Color(0xFF757575)
                                        )
                                    }
                                }
                                // Rating
                                val rating = restaurant.rating ?: 0.0
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(
                                        if (rating > 0) "%.1f".format(rating) else "جديد",
                                        fontSize = 11.sp,
                                        color = Color(0xFFF59E0B),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        Icons.Default.Star, null,
                                        modifier = Modifier.size(13.dp), tint = Color(0xFFF59E0B)
                                    )
                                }
                            }

                            // Min order
                            if ((restaurant.minOrderAmount ?: 0.0) > 0) {
                                Text(
                                    "الحد الأدنى للطلب: ${restaurant.minOrderAmount?.toLong()} ر.ي",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF757575),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Right
                                )
                            }
                        }
                    }

                    // ─── SEARCH BAR ───────────────────────────────────────────
                    item {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchChanged(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            placeholder = { Text("ابحث في المنيو...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, null, tint = Color(0xFF757575))
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1A5C3A),
                                unfocusedBorderColor = Color(0xFFEEEEEE),
                                containerColor = Color(0xFFF5F5F5)
                            )
                        )
                    }

                    // ─── CATEGORY CHIPS (circular, scroll to section) ─────────
                    if (uiState.searchQuery.isBlank() && uiState.categories.isNotEmpty()) {
                        item {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    "التصنيفات",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Right
                                )
                                Spacer(Modifier.height(8.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    reverseLayout = false
                                ) {
                                    items(uiState.categories, key = { it.id }) { cat ->
                                        val isActive = uiState.activeCategoryId == cat.id
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .width(64.dp)
                                                .clickable {
                                                    viewModel.setActiveCategory(cat.id)
                                                    coroutineScope.launch {
                                                        val idx = categoryIndexMap[cat.id] ?: return@launch
                                                        listState.animateScrollToItem(idx)
                                                    }
                                                }
                                        ) {
                                            Box(
                                                Modifier
                                                    .size(56.dp)
                                                    .clip(CircleShape)
                                                    .border(
                                                        2.dp,
                                                        if (isActive) Color(0xFF1A5C3A) else Color(0xFFEEEEEE),
                                                        CircleShape
                                                    )
                                                    .background(Color(0xFFF5F5F5))
                                            ) {
                                                val imgUrl = cat.imageUrl ?: getCategoryImage(cat.nameAr)
                                                AsyncImage(
                                                    model = imgUrl, contentDescription = cat.nameAr,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                            Spacer(Modifier.height(4.dp))
                                            Text(
                                                text = cat.nameAr,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = if (isActive) FontWeight.Black else FontWeight.Normal,
                                                color = if (isActive) Color(0xFF1A5C3A) else Color(0xFF1A1A1A),
                                                textAlign = TextAlign.Center,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                            HorizontalDivider(color = Color(0xFFEEEEEE))
                        }
                    }

                    // ─── MENU SECTIONS (per category) ─────────────────────────
                    uiState.categories.forEach { cat ->
                        val catItems = uiState.filteredItems.filter { it.categoryId == cat.id }
                        if (catItems.isEmpty()) return@forEach

                        // Section header
                        stickyHeader(key = "header_${cat.id}") {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = cat.nameAr,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Right
                                )
                            }
                        }

                        // Items (vertical cards)
                        items(catItems, key = { it.id }) { item ->
                            RestaurantMenuItemCard(
                                item = item,
                                cartQuantity = CartManager.items.value
                                    .find { c -> c.menuItem.id == item.id }?.quantity ?: 0,
                                onAdd = {
                                    viewModel.addToCart(
                                        item, restaurantId,
                                        uiState.restaurant?.nameAr ?: ""
                                    )
                                },
                                onDecrement = { CartManager.decrementItem(item.id) },
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // ─── FLOATING CART BAR ─────────────────────────────────────
                if (uiState.cartItemCount > 0) {
                    Box(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate(Screen.Cart.route) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A5C3A))
                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${uiState.cartTotal.toLong()} ر.ي",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "عرض السلة",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Surface(
                                    color = Color.White.copy(alpha = 0.25f),
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = "${uiState.cartItemCount}",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper: maps categoryId -> LazyColumn item index for scroll-to-section
fun buildCategoryIndexMap(
    categories: List<MenuCategory>,
    items: List<MenuItem>
): Map<String, Int> {
    // Fixed items before category sections:
    // 0=cover, 1=info, 2=search, 3=chips -> starts at index 4
    var idx = 4
    val map = mutableMapOf<String, Int>()
    categories.forEach { cat ->
        val catItems = items.filter { it.categoryId == cat.id }
        if (catItems.isEmpty()) return@forEach
        map[cat.id] = idx
        idx += 1 + catItems.size // 1 for header, rest for items
    }
    return map
}
