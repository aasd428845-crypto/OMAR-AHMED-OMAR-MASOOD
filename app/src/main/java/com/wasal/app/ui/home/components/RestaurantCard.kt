package com.wasal.app.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
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
import com.wasal.app.data.model.Restaurant

fun Long.toLocaleString(): String = String.format("%,d", this)

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.fillMaxSize()) {
            // LEFT: Cover image (100x100dp)
            Box(
                Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(Color(0xFFF0F0F0))
            ) {
                // Emoji fallback
                Text("🏪", modifier = Modifier.align(Alignment.Center), fontSize = 28.sp)
                // Actual image on top
                val imgUrl = restaurant.coverImage ?: restaurant.logoUrl
                if (imgUrl != null) {
                    AsyncImage(
                        model = imgUrl,
                        contentDescription = restaurant.nameAr,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // City badge — top end (RTL: top right)
                if (!restaurant.city.isNullOrBlank()) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp),
                        color = Color(0x8C000000),
                        shape = CircleShape
                    ) {
                        Text(
                            text = restaurant.city,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
                // Featured badge — bottom end
                if (restaurant.isFeatured == true) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp),
                        color = Color(0xFFF59E0B),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "مميز",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // RIGHT: Info
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Restaurant name
                Text(
                    text = restaurant.nameAr,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(4.dp))
                // Rating + Delivery time + Fee row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Delivery fee
                    val fee = restaurant.deliveryFee ?: 0.0
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFF888888)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = if (fee == 0.0) "مجاني"
                            else "${fee.toLong().toLocaleString()} ر.ي",
                            fontSize = 11.sp,
                            color = if (fee == 0.0) Color(0xFF52B788) else Color(0xFF888888),
                            fontWeight = if (fee == 0.0) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                    // Delivery time
                    if (restaurant.estimatedDeliveryTime != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = Color(0xFF888888)
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = "${restaurant.estimatedDeliveryTime} د",
                                fontSize = 11.sp,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                    // Rating
                    val rating = restaurant.rating ?: 0.0
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFFF59E0B)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = if (rating > 0) String.format("%.1f", rating) else "جديد",
                            fontSize = 11.sp,
                            color = Color(0xFFF59E0B),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
