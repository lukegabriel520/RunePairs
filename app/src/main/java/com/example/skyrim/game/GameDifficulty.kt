package com.example.skyrim.game

/**
 * Each difficulty fixes pair count, total cards (pairs × 2), grid width, and time limit.
 * Adjust values here if you want different pacing.
 */
enum class GameDifficulty {
    CASUAL,
    ADEPT,
    HELL;

    val config: DifficultyConfig
        get() = when (this) {
            CASUAL -> DifficultyConfig(pairCount = 4, timeLimitSeconds = 120, gridColumns = 3)
            ADEPT -> DifficultyConfig(pairCount = 6, timeLimitSeconds = 90, gridColumns = 3)
            HELL -> DifficultyConfig(pairCount = 9, timeLimitSeconds = 60, gridColumns = 3)
        }

    val displayName: String
        get() = when (this) {
            CASUAL -> "Casual"
            ADEPT -> "Adept"
            HELL -> "Hell"
        }

    companion object {
        fun fromRouteKey(key: String?): GameDifficulty {
            val k = key?.trim().orEmpty()
            return entries.find { it.name.equals(k, ignoreCase = true) } ?: ADEPT
        }
    }
}

data class DifficultyConfig(
    val pairCount: Int,
    val timeLimitSeconds: Int,
    val gridColumns: Int = 3
) {
    val totalCards: Int get() = pairCount * 2
}
