package com.wasal.app.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.wasal.app.data.model.DeliveryOffer

@Composable
fun OffersRow(
    offers: List<DeliveryOffer>,
    onOfferClick: (DeliveryOffer) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        // Section header
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {}) {
                Text(
                    text = "عرض الكل",
                    color = Color(0xFF1A5C3A),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalOffer,
                    contentDescription = null,
                    tint = Color(0xFF1A5C3A),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "عروض وخصومات",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Horizontal scroll of offer cards
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(offers, key = { it.id }) { offer ->
                OfferCard(offer = offer, onClick = { onOfferClick(offer) })
            }
        }
    }
}

@Composable
fun OfferCard(offer: DeliveryOffer, onClick: () -> Unit) {
    val badgeText = when {
        !offer.badgeText.isNullOrBlank() -> offer.badgeText
        offer.offerType == "free_delivery" -> "مجاني"
        offer.offerType == "percent_off_delivery" -> "خصم ${offer.discountPercent}%"
        offer.offerType == "percent_off_order" -> "خصم ${offer.discountPercent}%"
        else -> "عرض خاص"
    }
    val badgeColor = when (offer.offerType) {
        "free_delivery" -> Color(0xFF1B9E6E)
        "percent_off_delivery", "percent_off_order" -> Color(0xFFE53935)
        else -> Color(0xFF1B4332)
    }

    val fallbackImages = listOf(
        "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80",
        "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=400&q=80",
        "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400&q=80"
    )

    // Select stable fallback image URL based on offer ID hash code to avoid shifting on redraw
    val imageIndex = Math.abs(offer.id.hashCode()) % fallbackImages.size
    val imageUrl = offer.imageUrl ?: fallbackImages[imageIndex]

    Box(
        modifier = Modifier
            .width(175.dp)
            .height(112.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        // Background image with Matrix color scaling to darken it
        AsyncImage(
            model = imageUrl,
            contentDescription = offer.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.colorMatrix(
                ColorMatrix().apply { setToScale(0.52f, 0.52f, 0.52f, 1f) }
            )
        )

        // Badge — top right (RTL: actually top right/start depending on alignment)
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            color = badgeColor,
            shape = CircleShape
        ) {
            Text(
                text = badgeText,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black
            )
        }

        // Text at bottom
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xA6000000))
                    )
                )
                .padding(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = offer.title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
