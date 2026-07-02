package com.wasal.app.ui.notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.wasal.app.ui.navigation.Screen
import com.wasal.app.data.model.AppNotification
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

// Navigation resolver: same logic as PWA resolveNav
fun resolveNotificationNav(
    notifType: String?,
    data: JsonObject?
): String? {
    val orderId = data?.get("orderId")?.jsonPrimitive?.content
        ?: data?.get("order_id")?.jsonPrimitive?.content

    return when {
        notifType in listOf("order_status", "order_confirmed", "new_order", "order_received") ->
            Screen.OrdersHistory.route
        notifType == "order_assigned" && orderId != null ->
            Screen.OrderTracking.createRoute(orderId)
        notifType in listOf("promotion", "promo", "general") ->
            Screen.Home.route
        orderId != null -> Screen.OrdersHistory.route
        else -> null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController,
    userId: String,
    viewModel: NotificationsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadNotifications(userId)
        viewModel.subscribeToNotifications(userId)
    }

    DisposableEffect(userId) {
        onDispose { viewModel.unsubscribe(userId) }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                Surface(color = Color(0xFF1A5C3A), shadowElevation = 4.dp) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Mark all read button
                        if (uiState.unreadCount > 0) {
                            TextButton(
                                onClick = { viewModel.markAllAsRead(userId) },
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, null,
                                    tint = Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("قراءة الكل",
                                    color = Color.White.copy(alpha = 0.85f),
                                    style = MaterialTheme.typography.bodySmall)
                            }
                        } else { Spacer(Modifier.width(80.dp)) }

                        // Title + unread badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (uiState.unreadCount > 0) {
                                Surface(
                                    color = Color(0xFFEF4444),
                                    shape = CircleShape
                                ) {
                                    Text("${uiState.unreadCount}",
                                        Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        color = Color.White,
                                        fontSize = 10.sp, fontWeight = FontWeight.Black)
                                }
                            }
                            Text("الإشعارات",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black, color = Color.White)
                            Icon(Icons.Default.Notifications, null,
                                tint = Color.White, modifier = Modifier.size(22.dp))
                        }

                        // Back button
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White)
                        }
                    }
                }
            }
        ) { padding ->

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF1A5C3A))
                }
                return@Scaffold
            }

            if (uiState.notifications.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.NotificationsOff, null,
                            modifier = Modifier.size(64.dp), tint = Color(0xFFD1D5DB))
                        Spacer(Modifier.height(12.dp))
                        Text("لا توجد إشعارات حالياً",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF6B7280))
                    }
                }
                return@Scaffold
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .background(Color(0xFFF8F9FA)).padding(padding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(uiState.notifications, key = { it.id }) { notif ->
                    NotificationItem(
                        notif = notif,
                        onClick = {
                            viewModel.markAsRead(notif, userId)
                            val route = resolveNotificationNav(notif.notificationType, notif.data)
                            if (route != null) navController.navigate(route)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notif: AppNotification, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        color = if (notif.isUnread) Color(0xFFEFF6FF) else Color.White,
        shape = RoundedCornerShape(14.dp),
        border = if (notif.isUnread)
            BorderStroke(1.dp, Color(0xFF1A5C3A).copy(alpha = 0.25f))
        else BorderStroke(1.dp, Color(0xFFE5E7EB)),
        shadowElevation = if (notif.isUnread) 2.dp else 1.dp
    ) {
        Row(
            Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Emoji icon
            Text(notif.emoji,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 2.dp).alpha(if (notif.isUnread) 1f else 0.6f))

            // Content
            Column(Modifier.weight(1f)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(notif.timeAgo(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF9CA3AF))
                    // Unread dot + title
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (notif.isUnread) {
                            Box(Modifier.size(7.dp).background(Color(0xFF1A5C3A), CircleShape))
                        }
                        Text(
                            notif.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (notif.isUnread) FontWeight.Black else FontWeight.Normal,
                            color = if (notif.isUnread) Color(0xFF1A1A1A) else Color(0xFF6B7280),
                            textAlign = TextAlign.Right,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (!notif.body.isNullOrBlank()) {
                    Spacer(Modifier.height(3.dp))
                    Text(notif.body,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (notif.isUnread) Color(0xFF374151) else Color(0xFF9CA3AF),
                        maxLines = 2, overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth())
                }

                if (notif.isUnread && resolveNotificationNav(notif.notificationType, notif.data) != null) {
                    Spacer(Modifier.height(4.dp))
                    Text("اضغط للانتقال ←",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF1A5C3A))
                }
            }
        }
    }
}
