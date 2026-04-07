package com.example.skyrim.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skyrim.R
import com.example.skyrim.game.GameDifficulty
import com.example.skyrim.navigation.Screen
import com.example.skyrim.ui.screenContentPadding
import com.example.skyrim.ui.theme.SkyrimFont

@Composable
fun DifficultySelectScreen(navController: NavController) {
    val scrollTextColor = Color(0xFF2C1E14)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.other_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.5f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .screenContentPadding(extraTop = 20.dp, extraHorizontal = 12.dp, extraBottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Who seeks challenge remains outside the scroll
            Text(
                text = "Who seeks challenge?",
                style = TextStyle(
                    fontFamily = SkyrimFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black,
                        blurRadius = 16f
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp, bottom = 12.dp) // Added 10dp top padding as requested
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.96f)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.scroll_overlay),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                // All actual options are contained within the scroll
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 48.dp, bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "CHOOSE DIFFICULTY",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = SkyrimFont,
                            fontWeight = FontWeight.Bold,
                            color = scrollTextColor,
                            textAlign = TextAlign.Center,
                            letterSpacing = 2.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    GameDifficulty.entries.forEach { diff ->
                        val c = diff.config
                        DifficultyOption(
                            title = diff.displayName,
                            subtitle = "${c.pairCount} pairs · ${c.totalCards} cards · ${c.timeLimitSeconds}s",
                            onClick = {
                                navController.navigate(Screen.Game.createRoute(diff.name))
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF3E2723).copy(alpha = 0.15f),
                                RoundedCornerShape(4.dp)
                            )
                            .clickable { navController.navigateUp() }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "BACK",
                            fontSize = 18.sp,
                            fontFamily = SkyrimFont,
                            fontWeight = FontWeight.Bold,
                            color = scrollTextColor,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DifficultyOption(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF3E2723).copy(alpha = 0.08f), RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 20.sp,
            fontFamily = SkyrimFont,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C1E14),
            letterSpacing = 1.sp
        )
        Text(
            text = subtitle,
            style = TextStyle(
                fontSize = 13.sp,
                lineHeight = 16.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF5D4037),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
