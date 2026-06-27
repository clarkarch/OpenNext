package com.opennext.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.opennext.app.ui.screens.GameDetailScreen
import com.opennext.app.ui.screens.HomeScreen
import com.opennext.app.ui.screens.LibraryScreen
import com.opennext.app.ui.screens.LoginScreen
import com.opennext.app.ui.screens.SettingsScreen
import com.opennext.app.ui.screens.StreamLoadingScreen
import com.opennext.app.ui.screens.StreamViewScreen

@Composable
fun OpenNextNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onSignIn = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onGameClick = { gameId ->
                    navController.navigate(Screen.GameDetail.createRoute(gameId))
                },
                onLibraryClick = {
                    navController.navigate(Screen.Library.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
            )
        }

        composable(Screen.Library.route) {
            LibraryScreen(
                onGameClick = { gameId ->
                    navController.navigate(Screen.GameDetail.createRoute(gameId))
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Screen.GameDetail.route,
            arguments = listOf(navArgument("gameId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: return@composable
            GameDetailScreen(
                gameId = gameId,
                onPlay = { id ->
                    navController.navigate(Screen.StreamLoading.createRoute(id))
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Screen.StreamLoading.route,
            arguments = listOf(navArgument("gameId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: return@composable
            StreamLoadingScreen(
                gameId = gameId,
                onCancel = { navController.popBackStack() },
                onReady = { id ->
                    navController.navigate(Screen.StreamView.createRoute(id)) {
                        popUpTo(Screen.StreamLoading.createRoute(id)) { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = Screen.StreamView.route,
            arguments = listOf(navArgument("gameId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: return@composable
            StreamViewScreen(
                gameId = gameId,
                onExit = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
            )
        }
    }
}
