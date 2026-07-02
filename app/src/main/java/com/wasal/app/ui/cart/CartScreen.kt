package com.wasal.app.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wasal.app.ui.components.common.WasalTopBar
import com.wasal.app.ui.home.components.WasalBottomNav
import com.wasal.app.ui.cart.components.CartRestaurantGroup
import com.wasal.app.ui.navigation.Screen

@Composable
fun CartScreen(
    navController: NavController,
    userId: String?,
    viewModel: CartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadCart()
    }

    // Guest: redirect to login
    if (userId == null) {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Cart.route) { inclusive = true }
            }
        }
        return
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                WasalTopBar(
                    title = "سلة الطلبات",
                    onBackClick = { navController.navigateUp() }
                )
            },
            bottomBar = {
                WasalBottomNav(navController = navController, currentRoute = Screen.Cart.route)
            }
        ) { padding ->

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF1A5C3A))
                }
                return@Scaffold
            }

            // Empty cart state
            if (uiState.groups.isEmpty()) {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🛒", fontSize = 64.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "السلة فارغة",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "لم تقم بإضافة أي أصناف بعد",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF757575)
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = { navController.navigate(Screen.Home.route) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A5C3A)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Store, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("تصفح المطاعم")
                        }
                    }
                }
                return@Scaffold
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cart groups (one per restaurant)
                items(uiState.groups, key = { it.restaurantId }) { group ->
                    CartRestaurantGroup(
                        group = group,
                        onIncrement = { id -> viewModel.incrementItem(id) },
                        onDecrement = { id -> viewModel.decrementItem(id) },
                        onCheckout = {
                            navController.navigate(Screen.Checkout.createRoute(group.restaurantId))
                        }
                    )
                }
            }
        }
    }
}
