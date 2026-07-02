package com.wasal.app.ui.home.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.StoreMallDirectory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wasal.app.ui.navigation.Screen

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

val BOTTOM_NAV_ITEMS = listOf(
    BottomNavItem(Screen.Profile, Icons.Default.Person, "حسابي"),
    BottomNavItem(Screen.OrdersHistory, Icons.Default.ShoppingBag, "طلباتي"),
    BottomNavItem(Screen.Restaurants, Icons.Default.StoreMallDirectory, "المطاعم"),
    BottomNavItem(Screen.Home, Icons.Default.Home, "الرئيسية")
)

@Composable
fun WasalBottomNav(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        BOTTOM_NAV_ITEMS.forEach { item ->
            val isSelected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.screen.route) {
                        navController.navigate(item.screen.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1A5C3A),
                    selectedTextColor = Color(0xFF1A5C3A),
                    indicatorColor = Color(0xFFE8F5EE),
                    unselectedIconColor = Color(0xFF757575),
                    unselectedTextColor = Color(0xFF757575)
                )
            )
        }
    }
}
