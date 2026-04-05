package com.example.skyrim.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Menu : Screen("menu")
    object DifficultySelect : Screen("difficulty_select")
    object Game : Screen("game/{difficulty}") {
        fun createRoute(difficultyKey: String) = "game/${difficultyKey.lowercase()}"
    }

    object GameOver : Screen("game_over/{win}/{score}/{timeElapsed}/{flips}/{difficultyKey}") {
        fun createRoute(win: Boolean, score: Int, timeElapsed: Int, flips: Int, difficultyKey: String) =
            "game_over/$win/$score/$timeElapsed/$flips/$difficultyKey"
    }

    object Gallery : Screen("gallery")
    object Leaderboards : Screen("leaderboards")
    object Settings : Screen("settings")
    object Pause : Screen("pause")
}
