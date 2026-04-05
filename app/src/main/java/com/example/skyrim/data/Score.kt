package com.example.skyrim.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playerName: String,
    val score: Int,
    val difficulty: String,
    val time: String,
    val timestamp: Long = System.currentTimeMillis()
)
