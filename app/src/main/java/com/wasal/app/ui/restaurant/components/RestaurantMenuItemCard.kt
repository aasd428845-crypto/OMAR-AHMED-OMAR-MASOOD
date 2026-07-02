package com.wasal.app.ui.restaurant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.wasal.app.data.model.MenuItem

@Composable
fun RestaurantMenuItemCard(
    item: MenuItem,
    cartQuantity: Int,
    onAdd: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // LEFT: Item image (80x80dp square)
            Box(
                Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
            ) {
                if (item.imageUrl != null) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.nameAr,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("🍔", modifier = Modifier.align(Alignment.Center), fontSize = 28.sp)
                }
                if (item.hasDiscount) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp),
                        color = Color(0xFFE53935),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${item.discountPercent}%-",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            // RIGHT: Info + Price + Qty controls
            Column(Modifier.weight(1f)) {
                // Badges row
                if (item.isFeatured == true || item.isPopular == true) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (item.isPopular == true) {
                            Surface(color = Color(0xFFFFF8E1), shape = RoundedCornerShape(4.dp)) {
                                Text(
                                    text = "شائع 🔥",
                                    fontSize = 9.sp,
                                    color = Color(0xFFF59E0B),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        if (item.isFeatured == true) {
                            Surface(color = Color(0xFFE8F5EE), shape = RoundedCornerShape(4.dp)) {
                                Text(
                                    text = "مميز ⭐",
                                    fontSize = 9.sp,
                                    color = Color(0xFF1A5C3A),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                // Name
                Text(
                    text = item.nameAr,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )

                // Description
                if (!item.description.isNullOrBlank()) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF757575),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Price + Cart controls
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cart controls
                    if (cartQuantity > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // + button
                            Box(
                                Modifier
                                    .size(28.dp)
                                    .background(Color(0xFF1A5C3A), CircleShape)
                                    .clickable(onClick = onAdd),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            // Count
                            Text(
                                text = "$cartQuantity",
                                fontWeight = FontWeight.Black,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            // - button
                            Box(
                                Modifier
                                    .size(28.dp)
                                    .border(1.dp, Color(0xFF1A5C3A), CircleShape)
                                    .clickable(onClick = onDecrement),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = null,
                                    tint = Color(0xFF1A5C3A),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    } else {
                        // Simple + button
                        Box(
                            Modifier
                                .size(32.dp)
                                .background(Color(0xFF1A5C3A), CircleShape)
                                .clickable(onClick = onAdd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Price
                    Column(horizontalAlignment = Alignment.End) {
                        if (item.hasDiscount) {
                            Text(
                                text = "${item.price.toLong()} ر.ي",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF9E9E9E),
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                        Text(
                            text = "${item.effectivePrice.toLong()} ر.ي",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF1A5C3A)
                        )
                    }
                }
            }
        }
    }
}
