package com.wasal.app.ui.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object OtpVerify : Screen("otp_verify/{phone}") {
        fun createRoute(phone: String) = "otp_verify/$phone"
    }
    object Home : Screen("home")
    object Restaurants : Screen("restaurants")
    object Category : Screen("category/{categoryName}") {
        fun createRoute(categoryName: String) = 
            "category/${Uri.encode(categoryName)}"
    }
    object RestaurantMenu : Screen("restaurant_menu/{restaurantId}") {
        fun createRoute(restaurantId: String) = "restaurant_menu/$restaurantId"
    }
    object Cart : Screen("cart")
    object Checkout : Screen("checkout/{restaurantId}") {
        fun createRoute(restaurantId: String) = "checkout/$restaurantId"
    }
    object OrderTracking : Screen("order_tracking/{orderId}") {
        fun createRoute(orderId: String) = "order_tracking/$orderId"
    }
    object OrdersHistory : Screen("orders_history")
    object DeliveryRequest : Screen("delivery_request")
    object Profile : Screen("profile")
    object Addresses : Screen("addresses")
    object Notifications : Screen("notifications")
}
