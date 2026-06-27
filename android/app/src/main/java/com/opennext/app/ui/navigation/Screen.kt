package com.opennext.app.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Library : Screen("library")
    data object Settings : Screen("settings")
    data object GameDetail : Screen("game/{gameId}") {
        fun createRoute(gameId: String) = "game/$gameId"
    }
    data object StreamLoading : Screen("stream/loading/{gameId}") {
        fun createRoute(gameId: String) = "stream/loading/$gameId"
    }
    data object StreamView : Screen("stream/{gameId}") {
        fun createRoute(gameId: String) = "stream/$gameId"
    }
}
