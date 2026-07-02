package com.wasal.app.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.wasal.app.data.cart.CartManager
import com.wasal.app.ui.category.components.CategoryMenuItemCard
import com.wasal.app.ui.home.components.getCategoryImage
import com.wasal.app.ui.navigation.Screen

@Composable
fun CategoryScreen(
    navController: NavController,
    categoryName: String,
    viewModel: CategoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(categoryName) {
        viewModel.load(categoryName)
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                // No separate top bar — hero image IS the header
            }
        ) { padding ->
            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF1A5C3A))
                }
                return@Scaffold
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {

                // ─── Hero Header (200dp) ─────────────────────────────────
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        // Background image
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(uiState.categoryImage ?: getCategoryImage(categoryName))
                                .crossfade(true)
                                .build(),
                            contentDescription = categoryName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Dark gradient overlay
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color(0x44000000), Color(0xCC000000))
                                    )
                                )
                        )

                        // Back button — top right (RTL)
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(36.dp)
                                .background(Color(0x66000000), CircleShape)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White)
                        }

                        // Category info — bottom
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                "تصفح الأصناف",
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                categoryName,
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "${uiState.totalItems} صنف في ${uiState.filteredGroups.size} " +
                                    if (uiState.filteredGroups.size == 1) "مطعم" else "مطاعم",
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // ─── Search Bar ──────────────────────────────────────────
                item {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.onSearchChanged(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        placeholder = {
                            Text(
                                "ابحث في أصناف $categoryName...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, null, tint = Color(0xFF757575))
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1A5C3A),
                            unfocusedBorderColor = Color(0xFFEEEEEE),
                            containerColor = Color.White
                        )
                    )
                }

                // ─── Empty state ─────────────────────────────────────────
                if (uiState.filteredGroups.isEmpty() && !uiState.isLoading) {
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🍽️", fontSize = 48.sp)
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    "لا توجد أصناف حالياً",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "لم يتم إضافة أصناف لهذه الفئة بعد",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF757575)
                                )
                            }
                        }
                    }
                }

                // ─── Groups (one section per restaurant) ─────────────────
                uiState.filteredGroups.forEach { group ->
                    // Restaurant header
                    item(key = "header_${group.restaurant.id}") {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .clickable {
                                    navController.navigate(
                                        Screen.RestaurantMenu.createRoute(group.restaurant.id)
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Arrow (RTL: on left = leads)
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack, null,
                                tint = Color(0xFF1A5C3A), modifier = Modifier.size(18.dp)
                            )

                            // Restaurant name
                            Text(
                                text = group.restaurant.nameAr,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Right
                            )

                            // Logo
                            Box(
                                Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF0F0F0))
                            ) {
                                val logoUrl = group.restaurant.logoUrl
                                if (logoUrl != null) {
                                    AsyncImage(
                                        model = logoUrl, contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Text("🍽️", modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                    }

                    // Items horizontal row
                    item(key = "items_${group.restaurant.id}") {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            items(group.items, key = { it.id }) { item ->
                                CategoryMenuItemCard(
                                    item = item,
                                    onAddToCart = {
                                        CartManager.addItem(item, group.restaurant.id, group.restaurant.nameAr)
                                    }
                                )
                            }
                        }
                    }

                    // Divider between groups
                    item {
                        HorizontalDivider(
                            color = Color(0xFFEEEEEE),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
