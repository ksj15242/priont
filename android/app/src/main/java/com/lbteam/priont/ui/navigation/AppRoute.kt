package com.lbteam.priont.ui.navigation

sealed class AppRoute(val route: String) {

    data object Home : AppRoute(route = "home")

    data object Search : AppRoute(route = "search")

    data object Category : AppRoute(route = "category/{categoryId}") {
        fun createRoute(categoryId: String) = "category/$categoryId"
    }

    data object Product : AppRoute(route = "product/{productId}") {
        fun createRoute(productId: String) = "product/$productId"
    }
}