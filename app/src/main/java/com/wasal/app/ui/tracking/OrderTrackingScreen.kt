package com.wasal.app.ui.tracking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.wasal.app.ui.components.common.WasalTopBar
import com.wasal.app.ui.cart.components.PriceRow
import com.wasal.app.ui.navigation.Screen
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.double

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    navController: NavController,
    orderId: String,
    userId: String,
    viewModel: OrderTrackingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
        viewModel.subscribeToOrder(orderId)
    }

    DisposableEffect(orderId) {
        onDispose { viewModel.unsubscribeFromOrder(orderId) }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                WasalTopBar(title = "تتبع الطلب", onBackClick = { navController.navigateUp() })
            }
        ) { padding ->

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF1A5C3A))
                }
                return@Scaffold
            }

            val order = uiState.order ?: return@Scaffold

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // ─── STATUS STEPS CARD ──────────────────────────────────
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = order.statusColor.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        order.statusLabel,
                                        Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = order.statusColor
                                    )
                                }
                                Text("تتبع الطلب #${orderId.take(8)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF757575))
                            }

                            if (!order.isCancelled) {
                                Spacer(Modifier.height(20.dp))

                                val currentStepIdx = ORDER_STATUS_STEPS
                                    .indexOfFirst { it.key == order.status }

                                // Step timeline
                                ORDER_STATUS_STEPS.forEachIndexed { idx, step ->
                                    val isDone = idx <= currentStepIdx
                                    val isCurrent = idx == currentStepIdx

                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // Left: dot + line
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.width(24.dp)
                                        ) {
                                            // Step dot
                                            Box(
                                                Modifier
                                                    .size(if (isCurrent) 24.dp else 18.dp)
                                                    .background(
                                                        if (isDone) Color(0xFF1A5C3A) else Color(0xFFE5E7EB),
                                                        CircleShape
                                                    ),
                                                Alignment.Center
                                            ) {
                                                if (isDone) {
                                                    Text(step.emoji, fontSize = if (isCurrent) 12.sp else 10.sp)
                                                }
                                            }
                                            // Connector line
                                            if (idx < ORDER_STATUS_STEPS.lastIndex) {
                                                Box(
                                                    Modifier
                                                        .width(2.dp)
                                                        .height(32.dp)
                                                        .background(
                                                            if (idx < currentStepIdx) Color(0xFF1A5C3A)
                                                            else Color(0xFFE5E7EB)
                                                        )
                                                )
                                            }
                                        }

                                        // Right: label
                                        Column(modifier = Modifier.padding(bottom = 4.dp)) {
                                            Text(
                                                step.label,
                                                style = if (isCurrent) MaterialTheme.typography.bodyMedium
                                                        else MaterialTheme.typography.bodySmall,
                                                fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Normal,
                                                color = if (isDone) Color(0xFF1A5C3A) else Color(0xFF9CA3AF)
                                            )
                                        }
                                    }
                                }
                            } else {
                                // Cancelled
                                Spacer(Modifier.height(12.dp))
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Color(0xFFFEE2E2),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        "تم إلغاء هذا الطلب",
                                        Modifier.padding(12.dp),
                                        color = Color(0xFFDC2626),
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }

                // ─── RESTAURANT INFO ────────────────────────────────────
                if (uiState.restaurant != null) {
                    item {
                        val r = uiState.restaurant!!
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            colors = CardDefaults.cardColors(Color.White)
                        ) {
                            Row(
                                Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Logo
                                Box(
                                    Modifier.size(48.dp).clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF5F5F5))
                                ) {
                                    if (r.logoUrl != null) {
                                        AsyncImage(model = r.logoUrl, contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize())
                                    } else {
                                        Text("🍽️", Modifier.align(Alignment.Center))
                                    }
                                }
                                Column(Modifier.weight(1f)) {
                                    Text(r.nameAr, style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Right,
                                        modifier = Modifier.fillMaxWidth())
                                    Text("المطعم", style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF757575), textAlign = TextAlign.Right,
                                        modifier = Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }
                }

                // ─── ORDER DETAILS CARD ─────────────────────────────────
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("تفاصيل الطلب",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right)
                            Spacer(Modifier.height(10.dp))

                            // Items
                            val items = try {
                                order.items?.let { arr ->
                                    (0 until arr.size).map { i ->
                                        val obj = arr[i].jsonObject
                                        val name = obj["name_ar"]?.jsonPrimitive?.content ?: ""
                                        val qty = obj["quantity"]?.jsonPrimitive?.int ?: 1
                                        val price = obj["price"]?.jsonPrimitive?.double ?: 0.0
                                        Triple(name, qty, price)
                                    }
                                } ?: emptyList()
                            } catch (e: Exception) { emptyList() }

                            items.forEach { (name, qty, price) ->
                                Row(Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("${(price * qty).toLong()} ر.ي",
                                        style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                    Text("$name × $qty",
                                        style = MaterialTheme.typography.bodyMedium)
                                }
                                Spacer(Modifier.height(4.dp))
                            }

                            HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 8.dp))

                            // Pricing
                            PriceRow("المجموع الفرعي", "${order.subtotal.toLong()} ر.ي")
                            Spacer(Modifier.height(4.dp))
                            PriceRow("التوصيل",
                                if (order.deliveryFee == 0.0) "مجاني" else "${order.deliveryFee.toLong()} ر.ي",
                                if (order.deliveryFee == 0.0) Color(0xFF22C55E) else Color(0xFF1A1A1A))
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider(color = Color(0xFFEEEEEE))
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${order.total.toLong()} ر.ي",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black, color = Color(0xFF1A5C3A))
                                Text("الإجمالي",
                                    style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }

                            // Applied offer
                            if (!order.appliedOfferTitle.isNullOrBlank()) {
                                Spacer(Modifier.height(8.dp))
                                Surface(
                                    color = Color(0xFFE8F5EE), shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(0xFF52B788))
                                ) {
                                    Row(Modifier.fillMaxWidth().padding(8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("🎉", fontSize = 14.sp)
                                        Text(order.appliedOfferTitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF1B5E20), fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(1f), textAlign = TextAlign.Right)
                                    }
                                }
                            }

                            // Notes
                            if (!order.notes.isNullOrBlank()) {
                                Spacer(Modifier.height(8.dp))
                                Text("ملاحظات: ${order.notes}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF757575),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Right)
                            }

                            // Delivery address
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                                modifier = Modifier.fillMaxWidth()) {
                                Text(order.customerAddress,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF757575))
                                Icon(Icons.Default.LocationOn, null,
                                    tint = Color(0xFF1A5C3A), modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }

                // ─── REVIEW BUTTON (only when delivered) ───────────────
                if (order.isCompleted && order.restaurantId != null) {
                    item {
                        Button(
                            onClick = { viewModel.showReviewDialog() },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B))
                        ) {
                            Icon(Icons.Default.Star, null, tint = Color.White,
                                modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("قيّم تجربتك", color = Color.White,
                                fontWeight = FontWeight.Black,
                                style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }

            // ─── REVIEW DIALOG ───────────────────────────────────────
            if (uiState.showReviewDialog && order.restaurantId != null) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissReview() },
                    title = {
                        Text("تقييم المطعم", fontWeight = FontWeight.Black,
                            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right)
                    },
                    text = {
                        Column(horizontalAlignment = Alignment.End) {
                            Text("كيف كانت تجربتك؟",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right)
                            Spacer(Modifier.height(8.dp))
                            // Star rating row
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                (1..5).forEach { star ->
                                    IconButton(onClick = { viewModel.setRating(star) }) {
                                        Icon(
                                            if (star <= uiState.reviewRating) Icons.Default.Star
                                            else Icons.Default.StarOutline,
                                            null,
                                            tint = if (star <= uiState.reviewRating) Color(0xFFF59E0B)
                                                   else Color(0xFFD1D5DB),
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(
                                value = uiState.reviewText,
                                onValueChange = { viewModel.setReviewText(it) },
                                modifier = Modifier.fillMaxWidth().height(100.dp),
                                placeholder = { Text("اكتب تعليقك هنا (اختياري)") },
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF1A5C3A),
                                    unfocusedBorderColor = Color(0xFFEEEEEE)
                                )
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                scope.launch {
                                    viewModel.submitReview(userId, order.restaurantId)
                                }
                            },
                            enabled = !uiState.isSubmittingReview,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A5C3A))
                        ) {
                            if (uiState.isSubmittingReview)
                                CircularProgressIndicator(Modifier.size(18.dp), color = Color.White)
                            else Text("إرسال التقييم", color = Color.White)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.dismissReview() }) {
                            Text("إلغاء", color = Color(0xFF757575))
                        }
                    }
                )
            }
        }
    }
}
