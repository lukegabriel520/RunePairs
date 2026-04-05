package com.example.skyrim.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skyrim.R
import com.example.skyrim.auth.LocalDataManager
import com.example.skyrim.auth.ScoreEntry
import com.example.skyrim.navigation.Screen
import com.example.skyrim.ui.screenContentPadding
import com.example.skyrim.ui.theme.SkyrimFont

@Composable
fun LeaderboardsScreen(navController: NavController) {
    val context = LocalContext.current
    val dataManager = remember { LocalDataManager(context) }
    
    var selectedDifficulty by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }

    val difficulties = listOf("All", "Casual", "Adept", "Hell")

    val allScores = remember { dataManager.getHighScores() }
    
    val filteredEntries = remember(selectedDifficulty, allScores) {
        if (selectedDifficulty == "All") {
            allScores.sortedWith(
                compareByDescending<ScoreEntry> { it.score }
                    .thenBy { it.time }
                    .thenBy { it.flips }
            )
        } else {
            allScores.filter { it.difficulty.equals(selectedDifficulty, ignoreCase = true) }
                .sortedWith(
                    compareByDescending<ScoreEntry> { it.score }
                        .thenBy { it.time }
                        .thenBy { it.flips }
                )
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Image(
            painter = painterResource(id = R.drawable.other_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .screenContentPadding(extraTop = 28.dp, extraHorizontal = 16.dp, extraBottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack(Screen.Menu.route, inclusive = false) },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Leaderboards",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = SkyrimFont
                )

                Box {
                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_next),
                            contentDescription = "Filter",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color(0xFF2C2C2C))
                    ) {
                        difficulties.forEach { difficulty ->
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        difficulty, 
                                        color = if (selectedDifficulty == difficulty) Color.Yellow else Color.White,
                                        fontFamily = FontFamily.SansSerif
                                    ) 
                                },
                                onClick = {
                                    selectedDifficulty = difficulty
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            if (filteredEntries.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No records found for $selectedDifficulty.",
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(filteredEntries) { index, entry ->
                        LeaderboardItem(rank = index + 1, entry = entry)
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(rank: Int, entry: ScoreEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xE62C2C2C)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#$rank",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.width(40.dp),
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = entry.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = entry.score.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = entry.difficulty,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Flips: ${entry.flips}",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "Time: ${entry.time}",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }
    }
}
