package com.wasal.app.ui.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.wasal.app.data.model.MenuCategory

// Category image map (from PWA CategoryScroller.tsx)
val CATEGORY_IMAGES = mapOf(
    "برجر" to "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=200&q=80",
    "برغر" to "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=200&q=80",
    "برقر" to "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=200&q=80",
    "بيتزا" to "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=200&q=80",
    "حلويات" to "https://images.unsplash.com/photo-1551024601-bec78aea704b?w=200&q=80",
    "مشروبات" to "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=200&q=80",
    "شاورما" to "https://images.unsplash.com/photo-1561651823-34feb02250e4?w=200&q=80",
    "شورما" to "https://images.unsplash.com/photo-1561651823-34feb02250e4?w=200&q=80",
    "مندي" to "https://images.unsplash.com/photo-1633237308525-cd587cf71926?w=200&q=80",
    "دجاج" to "https://images.unsplash.com/photo-1598103442097-8b74394b95c7?w=200&q=80",
    "مأكولات بحرية" to "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=200&q=80",
    "سلطة" to "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=200&q=80",
    "عصير" to "https://images.unsplash.com/photo-1546173159-315724a31696?w=200&q=80",
    "قهوة" to "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=200&q=80"
)

fun getCategoryImage(nameAr: String): String =
    CATEGORY_IMAGES[nameAr]
        ?: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=200&q=80"

@Composable
fun CategoryScrollerSection(
    categories: List<MenuCategory>,
    onCategoryClick: (MenuCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Header
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {}) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "عرض الكل",
                        color = Color(0xFF1A5C3A),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color(0xFF1A5C3A),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Text(
                text = "الفئات",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black
            )
        }

        // Horizontal scroll of circular category icons
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories, key = { it.id }) { cat ->
                CategoryItem(category = cat, onClick = { onCategoryClick(cat) })
            }
        }
    }
}

@Composable
fun CategoryItem(category: MenuCategory, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(68.dp)
            .clickable(onClick = onClick)
    ) {
        // Circular image (64dp)
        Box(
            Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFFEEEEEE), CircleShape)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(category.imageUrl ?: getCategoryImage(category.nameAr))
                    .crossfade(true)
                    .build(),
                contentDescription = category.nameAr,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.height(4.dp))
        // Category name — truncated
        Text(
            text = category.nameAr,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 11.sp
        )
    }
}
