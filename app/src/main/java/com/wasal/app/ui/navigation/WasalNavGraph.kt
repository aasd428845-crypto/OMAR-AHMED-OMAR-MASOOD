package com.wasal.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wasal.app.ui.auth.SplashScreen
import com.wasal.app.ui.auth.LoginScreen
import com.wasal.app.ui.auth.RegisterScreen
import com.wasal.app.ui.auth.OtpScreen
import com.wasal.app.ui.home.HomeScreen
import com.wasal.app.ui.category.CategoryScreen
import com.wasal.app.ui.restaurant.RestaurantMenuScreen
import com.wasal.app.data.remote.WasalSupabase
import kotlinx.coroutines.delay

import com.wasal.app.ui.cart.CartScreen
import com.wasal.app.ui.checkout.CheckoutScreen
import com.wasal.app.ui.tracking.OrderTrackingScreen
import com.wasal.app.ui.orders.OrdersHistoryScreen
import com.wasal.app.ui.notifications.NotificationsScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WasalNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }

        composable(
            route = Screen.OtpVerify.route,
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpScreen(navController, phone)
        }

        composable(Screen.Home.route) {
            val currentUser = WasalSupabase.auth.currentUserOrNull()
            HomeScreen(
                navController = navController,
                userId = currentUser?.id,
                userCity = null
            )
        }

        composable(Screen.Restaurants.route) {
            PlaceholderScreen("Restaurants")
        }

        composable(
            route = Screen.Category.route,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryScreen(navController, categoryName)
        }

        composable(
            route = Screen.RestaurantMenu.route,
            arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
            RestaurantMenuScreen(navController, restaurantId)
        }

        composable(Screen.Cart.route) {
            val currentUser = WasalSupabase.auth.currentUserOrNull()
            CartScreen(
                navController = navController,
                userId = currentUser?.id
            )
        }

        composable(
            route = Screen.Checkout.route,
            arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
            val currentUser = WasalSupabase.auth.currentUserOrNull()

            if (currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route)
                }
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                var profileName by remember { mutableStateOf("") }
                var profilePhone by remember { mutableStateOf("") }
                var isLoadingProfile by remember { mutableStateOf(true) }

                LaunchedEffect(currentUser.id) {
                    try {
                        val profile = WasalSupabase.db.from("profiles")
                            .select {
                                filter { eq("user_id", currentUser.id) }
                            }
                            .decodeSingleOrNull<com.wasal.app.data.model.UserProfile>()
                        profileName = profile?.fullName ?: ""
                        profilePhone = profile?.phone ?: currentUser.phone ?: ""
                    } catch (e: Exception) {
                        profilePhone = currentUser.phone ?: ""
                    } finally {
                        isLoadingProfile = false
                    }
                }

                if (isLoadingProfile) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = androidx.compose.ui.graphics.Color(0xFF1A5C3A))
                    }
                } else {
                    CheckoutScreen(
                        navController = navController,
                        restaurantId = restaurantId,
                        userId = currentUser.id,
                        userFullName = profileName,
                        userPhone = profilePhone
                    )
                }
            }
        }

        composable(
            route = Screen.OrderTracking.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStack ->
            val orderId = backStack.arguments?.getString("orderId") ?: ""
            val currentUser = WasalSupabase.auth.currentUserOrNull()
            if (currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = androidx.compose.ui.graphics.Color(0xFF1A5C3A))
                }
            } else {
                OrderTrackingScreen(
                    navController = navController,
                    orderId = orderId,
                    userId = currentUser.id
                )
            }
        }

        composable(Screen.OrdersHistory.route) {
            val currentUser = WasalSupabase.auth.currentUserOrNull()
            if (currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = androidx.compose.ui.graphics.Color(0xFF1A5C3A))
                }
            } else {
                OrdersHistoryScreen(navController = navController, userId = currentUser.id)
            }
        }

        composable(Screen.DeliveryRequest.route) {
            PlaceholderScreen("Delivery Request")
        }

        composable(Screen.Profile.route) {
            PlaceholderScreen("Profile")
        }

        composable(Screen.Addresses.route) {
            PlaceholderScreen("Addresses")
        }

        composable(Screen.Notifications.route) {
            val currentUser = WasalSupabase.auth.currentUserOrNull()
            if (currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = androidx.compose.ui.graphics.Color(0xFF1A5C3A))
                }
            } else {
                NotificationsScreen(navController = navController, userId = currentUser.id)
            }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Screen: $name",
            style = MaterialTheme.typography.titleLarge
        )
    }
}
