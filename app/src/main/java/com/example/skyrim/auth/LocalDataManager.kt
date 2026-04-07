package com.example.skyrim.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

data class UserProfile(
    val email: String,
    val displayName: String = "Dragonborn",
    val personalBest: Int = 0
)

data class ScoreEntry(
    val name: String,
    val score: Int,
    val difficulty: String = "Adept",
    val time: String = "00:00",
    val flips: Int = 0
)

class LocalDataManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("skyrim_prefs", Context.MODE_PRIVATE)

    // Registry for users (email -> username)
    fun registerUser(email: String, username: String) {
        val allUsers = getAllUsers()
        allUsers.put(email, username)
        prefs.edit { putString("all_users", allUsers.toString()) }
    }

    // Save the active session
    fun saveUser(email: String, displayName: String, personalBest: Int = 0) {
        val userObj = JSONObject().apply {
            put("email", email)
            put("displayName", displayName)
            put("personalBest", personalBest)
        }
        prefs.edit { putString("current_user", userObj.toString()) }
    }

    fun getCurrentUser(): UserProfile? {
        val json = prefs.getString("current_user", null) ?: return null
        val obj = JSONObject(json)
        return UserProfile(
            obj.getString("email"),
            obj.getString("displayName"),
            obj.getInt("personalBest")
        )
    }

    fun isLoggedIn(): Boolean = prefs.contains("current_user")

    fun signOut() {
        prefs.edit { remove("current_user") }
    }

    // Authenticate against the registry
    fun authenticate(email: String, password: String): Boolean {
        val allUsers = getAllUsers()
        if (allUsers.has(email)) {
            val username = allUsers.getString(email)
            // Simulating successful login: save session
            saveUser(email, username)
            return true
        }
        return false
    }

    private fun getAllUsers(): JSONObject {
        val json = prefs.getString("all_users", null) ?: return JSONObject()
        return JSONObject(json)
    }

    // High Scores
    fun saveScore(score: Int, difficulty: String, time: String, flips: Int) {
        val user = getCurrentUser() ?: return
        val currentScores = getHighScores().toMutableList()
        currentScores.add(ScoreEntry(user.displayName, score, difficulty, time, flips))
        
        val sortedScores = currentScores.sortedWith(
            compareByDescending<ScoreEntry> { it.score }
                .thenBy { it.time }
                .thenBy { it.flips }
        ).take(20)
        
        val array = JSONArray()
        sortedScores.forEach { entry ->
            val obj = JSONObject().apply {
                put("name", entry.name)
                put("score", entry.score)
                put("difficulty", entry.difficulty)
                put("time", entry.time)
                put("flips", entry.flips)
            }
            array.put(obj)
        }
        prefs.edit { putString("high_scores", array.toString()) }

        if (score > user.personalBest) {
            saveUser(user.email, user.displayName, score)
        }
    }

    fun getHighScores(): List<ScoreEntry> {
        val json = prefs.getString("high_scores", null) ?: return emptyList()
        val array = JSONArray(json)
        val list = mutableListOf<ScoreEntry>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(ScoreEntry(
                obj.getString("name"),
                obj.getInt("score"),
                obj.getString("difficulty"),
                obj.getString("time"),
                obj.optInt("flips", 0)
            ))
        }
        return list
    }

    // Settings
    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit { putBoolean("sound_enabled", enabled) }
    }

    fun isSoundEnabled(): Boolean = prefs.getBoolean("sound_enabled", true)

    fun setMusicEnabled(enabled: Boolean) {
        prefs.edit { putBoolean("music_enabled", enabled) }
    }

    fun isMusicEnabled(): Boolean = prefs.getBoolean("music_enabled", true)

    fun wipeData() {
        prefs.edit { clear() }
    }
}
