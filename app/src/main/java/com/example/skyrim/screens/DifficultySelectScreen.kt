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
                .screenContentPadding(extraTop = 44.dp, extraHorizontal = 12.dp, extraBottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Who seeks challenge?",
                style = TextStyle(
                    fontFamily = SkyrimFont, // Updated to use SkyrimFont
                    fontWeight = FontWeight.Medium,
                    fontSize = 28.sp,
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.9f),
                        blurRadius = 16f
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.96f)
                    .weight(1f)
                    .padding(bottom = 6.dp),
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
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 10.dp, end = 10.dp, top = 40.dp, bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                    GameDifficulty.entries.forEach { diff ->
                        val c = diff.config
                        DifficultyOption(
                            title = diff.displayName,
                            subtitle = "${c.pairCount} pairs · ${c.totalCards} cards · ${c.timeLimitSeconds}s",
                            onClick = {
                                navController.navigate(Screen.Game.createRoute(diff.name))
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF3E2723).copy(alpha = 0.1f),
                                RoundedCornerShape(4.dp)
                            )
                            .clickable { navController.navigateUp() }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "BACK",
                            fontSize = 18.sp,
                            fontFamily = SkyrimFont, // Updated to use SkyrimFont
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
            .padding(vertical = 16.dp, horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 22.sp,
            fontFamily = SkyrimFont, // Updated to use SkyrimFont
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C1E14),
            letterSpacing = 1.sp
        )
        Text(
            text = subtitle,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily.SansSerif, // Updated to use SansSerif for description
                fontWeight = FontWeight.Medium,
                color = Color(0xFF5D4037),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
