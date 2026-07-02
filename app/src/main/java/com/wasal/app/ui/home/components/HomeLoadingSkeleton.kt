package com.wasal.app.ui.home.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun HomeLoadingSkeleton(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        // Banner skeleton
        item {
            Box(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        // Offers row skeleton
        item {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(3) {
                    Box(
                        Modifier
                            .width(175.dp)
                            .height(112.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
        // Categories skeleton
        item {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(5) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.size(64.dp).clip(CircleShape).shimmerEffect())
                        Spacer(Modifier.height(4.dp))
                        Box(
                            Modifier
                                .width(48.dp)
                                .height(10.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .shimmerEffect()
                        )
                    }
                }
            }
        }
        // Restaurant cards skeleton
        items(4) {
            Box(
                Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
    }
}

// Shimmer effect extension
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer")
    
    // Fallback dimension so animation doesn't start at zero and get stuck
    val widthValue = if (size.width > 0) size.width.toFloat() else 400f
    val heightValue = if (size.height > 0) size.height.toFloat() else 200f

    val startOffsetX by transition.animateFloat(
        initialValue = -2 * widthValue,
        targetValue = 2 * widthValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing)
        ),
        label = "shimmer_x"
    )
    
    background(
        Brush.linearGradient(
            colors = listOf(Color(0xFFE0E0E0), Color(0xFFF5F5F5), Color(0xFFE0E0E0)),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + widthValue, heightValue)
        )
    ).onGloballyPositioned { size = it.size }
}
