package com.example.skyrim.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skyrim.R
import com.example.skyrim.auth.LocalDataManager
import com.example.skyrim.game.GameDifficulty
import com.example.skyrim.navigation.Screen
import java.util.Locale

@Composable
fun GameOverScreen(
    navController: NavController,
    win: Boolean,
    score: Int,
    timeElapsed: Int,
    flips: Int,
    difficultyKey: String
) {
    val context = LocalContext.current
    val dataManager = remember { LocalDataManager(context) }
    
    val goldColor = Color(0xFFD4AF37)
    val scrollTextColor = Color(0xFF2C1E14)
    val scrollDetailColor = Color(0xFF5D4037)
    val difficulty = GameDifficulty.fromRouteKey(difficultyKey)
    val difficultyLabel = difficulty.displayName
    val timeFormatted = formatTime(timeElapsed)
    val flavorTitle = if (win) "VICTORY" else "DEFEAT"
    val flavorSub = if (win) "QUEST COMPLETE" else "TIME HAS RUN OUT"

    // Save score when screen is loaded
    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (win) {
            dataManager.saveScore(score, difficultyLabel, timeFormatted, flips)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.game_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.72f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.scroll_overlay),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(horizontal = 20.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = flavorTitle,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = scrollTextColor,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = flavorSub,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = scrollDetailColor,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    GameOverStatRow("DIFFICULTY", difficultyLabel.uppercase(), scrollDetailColor, scrollTextColor)
                    GameOverStatRow("FINAL SCORE", score.toString(), scrollDetailColor, scrollTextColor)
                    GameOverStatRow("TOTAL FLIPS", flips.toString(), scrollDetailColor, scrollTextColor)
                    GameOverStatRow(
                        label = if (win) "TIME USED" else "TIME ELAPSED",
                        value = timeFormatted,
                        labelColor = scrollDetailColor,
                        valueColor = scrollTextColor
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate(Screen.DifficultySelect.route) {
                                popUpTo(Screen.Menu.route) { inclusive = false }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E2723)),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            "START NEW QUEST",
                            color = goldColor,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 1.sp,
                            fontSize = 15.sp
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            navController.popBackStack(Screen.Menu.route, inclusive = false)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3E2723)),
                        border = BorderStroke(1.5.dp, Color(0xFF3E2723)),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            "RETURN TO MENU",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 1.sp,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GameOverStatRow(
    label: String,
    value: String,
    labelColor: Color,
    valueColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = labelColor,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                .height(1.dp)
                .background(labelColor.copy(alpha = 0.3f))
        )

        Text(
            text = value,
            fontSize = 17.sp,
            color = valueColor,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format(Locale.getDefault(), "%d:%02d", m, s)
}
