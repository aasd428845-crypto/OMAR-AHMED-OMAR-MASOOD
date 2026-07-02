package com.wasal.app.ui.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.wasal.app.data.model.CartItem
import com.wasal.app.ui.cart.RestaurantCartGroup

@Composable
fun CartRestaurantGroup(
    group: RestaurantCartGroup,
    onIncrement: (String) -> Unit,
    onDecrement: (String) -> Unit,
    onCheckout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {

            // Restaurant header
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Logo
                Box(
                    Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5))
                ) {
                    if (group.logoUrl != null) {
                        AsyncImage(
                            model = group.logoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text("🍽️", modifier = Modifier.align(Alignment.Center))
                    }
                }
                Text(
                    group.restaurantName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Right
                )
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(Modifier.height(12.dp))

            // Items list
            group.items.forEach { cartItem ->
                CartItemRow(
                    cartItem = cartItem,
                    onIncrement = { onIncrement(cartItem.menuItem.id) },
                    onDecrement = { onDecrement(cartItem.menuItem.id) }
                )
                Spacer(Modifier.height(8.dp))
            }

            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(Modifier.height(12.dp))

            // Offer banner (if active)
            if (group.activeOffer != null && group.offerApplies) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFE8F5EE),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFF52B788))
                ) {
                    Row(
                        Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🎉", fontSize = 18.sp)
                        Text(
                            group.activeOffer.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Right
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
            }

            // Offer progress (not yet reached)
            if (group.activeOffer != null && !group.offerApplies &&
                (group.activeOffer.minOrderAmount ?: 0.0) > 0
            ) {
                val remaining = (group.activeOffer.minOrderAmount ?: 0.0) - group.subtotal
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFF8E1),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFCA28))
                ) {
                    Row(
                        Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🎁", fontSize = 16.sp)
                        Column(Modifier.weight(1f)) {
                            Text(
                                group.activeOffer.title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF795548),
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                "أضف ${remaining.toLong()} ر.ي للحصول على هذا العرض",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF795548),
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }

            // Price breakdown
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                PriceRow("المجموع الفرعي", "${group.subtotal.toLong()} ر.ي")

                // Delivery fee row
                val feeText = when {
                    group.offerApplies && group.computedDeliveryFee == 0.0 ->
                        "مجاني 🎉"
                    group.offerApplies && group.computedDeliveryFee < group.deliveryFee ->
                        "${group.computedDeliveryFee.toLong()} ر.ي"
                    group.deliveryFee == 0.0 -> "مجاني"
                    else -> "${group.deliveryFee.toLong()} ر.ي"
                }
                val feeColor = if (group.computedDeliveryFee == 0.0) Color(0xFF52B788) else Color(0xFF1A1A1A)
                PriceRow("رسوم التوصيل", feeText, valueColor = feeColor)

                HorizontalDivider(color = Color(0xFFEEEEEE))

                // Total
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${group.total.toLong()} ر.ي",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1A5C3A)
                    )
                    Text(
                        "الإجمالي",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Min order warning
            if (group.minOrderAmount > 0 && group.subtotal < group.minOrderAmount) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "الحد الأدنى للطلب ${group.minOrderAmount.toLong()} ر.ي",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE53935),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
            }

            Spacer(Modifier.height(12.dp))

            // Checkout button
            Button(
                onClick = onCheckout,
                enabled = group.minOrderAmount == 0.0 || group.subtotal >= group.minOrderAmount,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A5C3A))
            ) {
                Text(
                    "متابعة الطلب",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, null,
                    tint = Color.White, modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Image
        if (cartItem.menuItem.imageUrl != null) {
            Box(
                Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF5F5F5))
            ) {
                AsyncImage(
                    model = cartItem.menuItem.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Name + price
        Column(Modifier.weight(1f)) {
            Text(
                cartItem.menuItem.nameAr,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "${cartItem.menuItem.effectivePrice.toLong()} ر.ي",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF757575),
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Qty controls
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // + button
            Box(
                Modifier
                    .size(28.dp)
                    .background(Color(0xFF1A5C3A), CircleShape)
                    .clickable(onClick = onIncrement),
                Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add, null, tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                "${cartItem.quantity}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.widthIn(min = 24.dp),
                textAlign = TextAlign.Center
            )
            // - button
            Box(
                Modifier
                    .size(28.dp)
                    .border(1.dp, Color(0xFF1A5C3A), CircleShape)
                    .clickable(onClick = onDecrement),
                Alignment.Center
            ) {
                Icon(
                    Icons.Default.Remove, null, tint = Color(0xFF1A5C3A),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Line total
        Text(
            "${(cartItem.menuItem.effectivePrice * cartItem.quantity).toLong()} ر.ي",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.widthIn(min = 60.dp),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun PriceRow(label: String, value: String, valueColor: Color = Color(0xFF1A1A1A)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(value, style = MaterialTheme.typography.bodyMedium, color = valueColor)
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF757575))
    }
}
