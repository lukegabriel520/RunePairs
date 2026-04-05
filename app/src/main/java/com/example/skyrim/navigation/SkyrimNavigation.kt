package com.example.skyrim.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.skyrim.game.GameDifficulty
import com.example.skyrim.screens.*

@Composable
fun SkyrimNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }
        composable(Screen.Menu.route) {
            MenuScreen(navController = navController)
        }
        composable(Screen.DifficultySelect.route) {
            DifficultySelectScreen(navController = navController)
        }
        composable(
            route = Screen.Game.route,
            arguments = listOf(
                navArgument("difficulty") {
                    type = NavType.StringType
                    defaultValue = GameDifficulty.ADEPT.name.lowercase()
                }
            )
        ) { backStackEntry ->
            val key = backStackEntry.arguments?.getString("difficulty")
            val difficulty = GameDifficulty.fromRouteKey(key)
            GameScreen(navController = navController, difficulty = difficulty)
        }
        composable(Screen.Gallery.route) {
            GalleryScreen(navController = navController)
        }
        composable(Screen.Leaderboards.route) {
            LeaderboardsScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(Screen.Pause.route) {
            PauseScreen(navController = navController)
        }
        composable(
            route = Screen.GameOver.route,
            arguments = listOf(
                navArgument("win") { type = NavType.BoolType },
                navArgument("score") { type = NavType.IntType },
                navArgument("timeElapsed") { type = NavType.IntType },
                navArgument("flips") { type = NavType.IntType },
                navArgument("difficultyKey") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val win = backStackEntry.arguments?.getBoolean("win") ?: false
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val timeElapsed = backStackEntry.arguments?.getInt("timeElapsed") ?: 0
            val flips = backStackEntry.arguments?.getInt("flips") ?: 0
            val difficultyKey = backStackEntry.arguments?.getString("difficultyKey").orEmpty()
            GameOverScreen(
                navController = navController,
                win = win,
                score = score,
                timeElapsed = timeElapsed,
                flips = flips,
                difficultyKey = difficultyKey
            )
        }
    }
}
