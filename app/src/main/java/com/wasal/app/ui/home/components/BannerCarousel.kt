package com.wasal.app.ui.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.wasal.app.data.model.DeliveryBanner
import kotlinx.coroutines.delay

val FALLBACK_BANNERS = listOf(
    DeliveryBanner(
        id = "f1",
        title = "خصم 30% على أول طلب",
        subtitle = "اطلب من مطاعمك المفضلة الآن",
        imageUrl = "https://images.unsplash.com/photo-1526367790999-0150786686a2?w=700&q=80",
        badgeText = "عرض محدود",
        isActive = true
    ),
    DeliveryBanner(
        id = "f2",
        title = "توصيل سريع في 30 دقيقة",
        subtitle = "بقالتك وصيدليتك تصل لبابك",
        imageUrl = "https://images.unsplash.com/photo-1542838132-92c53300491e?w=700&q=80",
        badgeText = "حصري",
        isActive = true
    )
)

@Composable
fun BannerCarousel(
    banners: List<DeliveryBanner>,
    onBannerClick: (linkTab: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val list = banners.ifEmpty { FALLBACK_BANNERS }
    var currentPage by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { list.size })

    // Auto-scroll every 4.5 seconds
    LaunchedEffect(pagerState.pageCount) {
        while (true) {
            delay(4500)
            if (list.isNotEmpty()) {
                val next = (pagerState.currentPage + 1) % list.size
                pagerState.animateScrollToPage(next)
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        currentPage = pagerState.currentPage
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            HorizontalPager(state = pagerState) { page ->
                val banner = list[page]
                Box(
                    Modifier
                        .fillMaxSize()
                        .clickable { onBannerClick(banner.linkTab ?: "restaurants") }
                ) {
                    // Background image
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(banner.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = banner.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Dark gradient overlay (bottom to top)
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xCC000000)),
                                    startY = 0f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )

                    // Badge — top right
                    if (!banner.badgeText.isNullOrBlank()) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp),
                            color = Color.White.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(99.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = banner.badgeText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Title + Subtitle — bottom right (RTL)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = banner.title,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Right
                        )
                        if (!banner.subtitle.isNullOrBlank()) {
                            Text(
                                text = banner.subtitle,
                                color = Color.White.copy(alpha = 0.85f),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Right
                            )
                        }
                    }
                }
            }

            // Dot indicators — bottom center
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                list.indices.forEach { i ->
                    Box(
                        Modifier
                            .animateContentSize()
                            .height(6.dp)
                            .width(if (i == currentPage) 20.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (i == currentPage) Color.White
                                else Color.White.copy(alpha = 0.5f)
                            )
                    )
                }
            }
        }
    }
}
