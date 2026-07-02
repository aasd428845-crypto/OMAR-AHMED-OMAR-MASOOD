package com.wasal.app.ui.orders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.wasal.app.data.model.DeliveryOrder
import com.wasal.app.ui.components.common.WasalTopBar
import com.wasal.app.ui.home.components.WasalBottomNav
import com.wasal.app.ui.navigation.Screen
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.int

@Composable
fun OrdersHistoryScreen(
    navController: NavController,
    userId: String,
    viewModel: OrdersHistoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadOrders(userId)
        viewModel.subscribeToUpdates(userId)
    }

    DisposableEffect(userId) {
        onDispose { viewModel.unsubscribe(userId) }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                WasalTopBar(title = "طلباتي", onBackClick = { navController.navigateUp() })
            },
            bottomBar = {
                WasalBottomNav(navController, Screen.OrdersHistory.route)
            }
        ) { padding ->

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF1A5C3A))
                }
                return@Scaffold
            }

            if (uiState.orders.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📦", fontSize = 64.sp)
                        Spacer(Modifier.height(16.dp))
                        Text("لا توجد طلبات بعد",
                            style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("ابدأ طلبك الأول الآن",
                            style = MaterialTheme.typography.bodyMedium, color = Color(0xFF757575))
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = { navController.navigate(Screen.Restaurants.route) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A5C3A)),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("تصفح المطاعم") }
                    }
                }
                return@Scaffold
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.orders, key = { it.id }) { order ->
                    OrderHistoryCard(
                        order = order,
                        isCancelling = uiState.cancellingId == order.id,
                        onClick = {
                            navController.navigate(Screen.OrderTracking.createRoute(order.id))
                        },
                        onCancel = {
                            if (order.isCancellable) viewModel.cancelOrder(order.id, userId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(
    order: DeliveryOrder,
    isCancelling: Boolean,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(Modifier.padding(14.dp)) {
            // Header row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status badge
                Surface(
                    color = order.statusColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(order.statusLabel,
                        Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold, color = order.statusColor)
                }
                // Order ID + date
                Column(horizontalAlignment = Alignment.End) {
                    Text("#${order.id.take(8)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF757575), fontWeight = FontWeight.Bold)
                    val dateText = try {
                        order.createdAt?.substringBefore("T") ?: ""
                    } catch (e: Exception) { "" }
                    if (dateText.isNotEmpty()) {
                        Text(dateText, style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF9CA3AF))
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Items summary
            val items = try {
                order.items?.let { arr ->
                    (0 until minOf(arr.size, 3)).map { i ->
                        val obj = arr[i].jsonObject
                        val name = obj["name_ar"]?.jsonPrimitive?.content ?: ""
                        val qty = obj["quantity"]?.jsonPrimitive?.int ?: 1
                        "$name × $qty"
                    }
                } ?: emptyList()
            } catch (e: Exception) { emptyList() }

            if (items.isNotEmpty()) {
                Text(
                    items.joinToString("، ") +
                        if ((order.items?.size ?: 0) > 3) " وأكثر..." else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF374151),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
                Spacer(Modifier.height(8.dp))
            }

            // Price + cancel row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cancel button
                if (order.isCancellable) {
                    OutlinedButton(
                        onClick = onCancel,
                        enabled = !isCancelling,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFEF4444)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFEF4444)
                        ),
                        modifier = Modifier.height(34.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        if (isCancelling) {
                            CircularProgressIndicator(Modifier.size(14.dp), color = Color(0xFFEF4444))
                        } else {
                            Text("إلغاء", style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Track button for active orders
                    if (!order.isCompleted && !order.isCancelled) {
                        TextButton(onClick = onClick) {
                            Text("تتبع الطلب ←",
                                color = Color(0xFF1A5C3A),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Spacer(Modifier.width(1.dp))
                    }
                }

                // Total
                Text("${order.total.toLong()} ر.ي",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black, color = Color(0xFF1A5C3A))
            }
        }
    }
}
