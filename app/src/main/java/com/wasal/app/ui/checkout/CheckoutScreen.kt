package com.wasal.app.ui.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wasal.app.ui.components.common.WasalTopBar
import com.wasal.app.ui.cart.components.PriceRow
import com.wasal.app.ui.navigation.Screen

@Composable
fun CheckoutScreen(
    navController: NavController,
    restaurantId: String,
    userId: String,
    userFullName: String,
    userPhone: String,
    viewModel: CheckoutViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(restaurantId) {
        viewModel.loadCheckout(restaurantId, userId, userPhone)
    }

    // Navigate on order placed
    LaunchedEffect(uiState.state) {
        if (uiState.state is CheckoutState.OrderPlaced) {
            val orderId = (uiState.state as CheckoutState.OrderPlaced).orderId
            navController.navigate(Screen.OrderTracking.createRoute(orderId)) {
                popUpTo(Screen.RestaurantMenu.route) { inclusive = false }
            }
        }
    }

    val snackbarHost = remember { SnackbarHostState() }
    LaunchedEffect(uiState.state) {
        if (uiState.state is CheckoutState.Error) {
            snackbarHost.showSnackbar((uiState.state as CheckoutState.Error).message)
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                WasalTopBar(title = "إتمام الطلب", onBackClick = { navController.navigateUp() })
            },
            snackbarHost = { SnackbarHost(snackbarHost) },
            bottomBar = {
                // Place order button pinned at bottom
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Button(
                        onClick = { viewModel.placeOrder(userId, userFullName) },
                        enabled = uiState.selectedAddress != null &&
                                uiState.state == CheckoutState.ReadyToOrder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A5C3A))
                    ) {
                        if (uiState.state == CheckoutState.PlacingOrder) {
                            CircularProgressIndicator(Modifier.size(22.dp), color = Color.White)
                        } else {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${uiState.total.toLong()} ر.ي",
                                    fontWeight = FontWeight.Bold, color = Color.White,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    "تأكيد الطلب",
                                    fontWeight = FontWeight.Black, color = Color.White,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        ) { padding ->

            if (uiState.state == CheckoutState.LoadingData) {
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF1A5C3A))
                }
                return@Scaffold
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // ─── ORDER SUMMARY CARD ────────────────────────────────
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    uiState.restaurantName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Right
                                )
                                Icon(
                                    Icons.Default.ShoppingCart, null,
                                    tint = Color(0xFF1A5C3A)
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                            HorizontalDivider(color = Color(0xFFEEEEEE))
                            Spacer(Modifier.height(10.dp))

                            // Items summary
                            uiState.cartItems.forEach { ci ->
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "${(ci.menuItem.effectivePrice * ci.quantity).toLong()} ر.ي",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "${ci.menuItem.nameAr}  ×${ci.quantity}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                            }

                            // Offer banner
                            val offer = uiState.activeOffer
                            val offerApplies = offer != null &&
                                    uiState.subtotal >= (offer.minOrderAmount ?: 0.0)

                            if (offer != null && offerApplies) {
                                Spacer(Modifier.height(8.dp))
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Color(0xFFE8F5EE),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(0xFF52B788))
                                ) {
                                    Row(
                                        Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("🎉", fontSize = 18.sp)
                                        Text(
                                            offer.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1B5E20),
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Right
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(10.dp))
                            HorizontalDivider(color = Color(0xFFEEEEEE))
                            Spacer(Modifier.height(10.dp))

                            // Price rows
                            PriceRow("المجموع الفرعي", "${uiState.subtotal.toLong()} ر.ي")
                            Spacer(Modifier.height(4.dp))
                            PriceRow(
                                "رسوم التوصيل",
                                if (uiState.deliveryFee == 0.0) "مجاني 🎉"
                                else "${uiState.deliveryFee.toLong()} ر.ي",
                                valueColor = if (uiState.deliveryFee == 0.0) Color(0xFF52B788)
                                else Color(0xFF1A1A1A)
                            )
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider(color = Color(0xFFEEEEEE))
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    "${uiState.total.toLong()} ر.ي",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black, color = Color(0xFF1A5C3A)
                                )
                                Text(
                                    "الإجمالي",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // ─── ADDRESS SELECTOR CARD ─────────────────────────────
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
                                Icon(Icons.Default.LocationOn, null, tint = Color(0xFF1A5C3A))
                                Text(
                                    "عنوان التوصيل",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(Modifier.height(12.dp))

                            if (uiState.addresses.isEmpty()) {
                                // No addresses saved
                                Text(
                                    "لا توجد عناوين محفوظة",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF757575),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                uiState.addresses.forEach { address ->
                                    val isSelected = uiState.selectedAddress?.id == address.id
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        color = if (isSelected) Color(0xFFE8F5EE) else Color(0xFFF8F9FA),
                                        shape = RoundedCornerShape(10.dp),
                                        border = if (isSelected) BorderStroke(2.dp, Color(0xFF1A5C3A))
                                        else BorderStroke(1.dp, Color(0xFFEEEEEE))
                                    ) {
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .clickable { viewModel.selectAddress(address) }
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            RadioButton(
                                                selected = isSelected,
                                                onClick = { viewModel.selectAddress(address) },
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = Color(0xFF1A5C3A)
                                                )
                                            )
                                            Column(Modifier.weight(1f)) {
                                                Text(
                                                    address.addressName,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Right,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                                Text(
                                                    address.displayLabel,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color(0xFF757575),
                                                    textAlign = TextAlign.Right,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }
                                    }
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }

                // ─── PHONE & NOTES CARD ────────────────────────────────
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            // Phone
                            Text(
                                "رقم الهاتف",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right
                            )
                            Spacer(Modifier.height(6.dp))
                            OutlinedTextField(
                                value = uiState.phone,
                                onValueChange = { viewModel.updatePhone(it) },
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, null, tint = Color(0xFF757575))
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF1A5C3A),
                                    unfocusedBorderColor = Color(0xFFEEEEEE)
                                ),
                                textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Ltr)
                            )
                            Spacer(Modifier.height(14.dp))

                            // Notes
                            Text(
                                "ملاحظات (اختياري)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right
                            )
                            Spacer(Modifier.height(6.dp))
                            OutlinedTextField(
                                value = uiState.notes,
                                onValueChange = { viewModel.updateNotes(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                placeholder = { Text("مثال: اطرق الجرس عند الوصول...") },
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF1A5C3A),
                                    unfocusedBorderColor = Color(0xFFEEEEEE)
                                )
                            )
                        }
                    }
                }

                // Extra space for bottom button
                item { Spacer(Modifier.height(32.dp)) }
            }
        }
    }
}
