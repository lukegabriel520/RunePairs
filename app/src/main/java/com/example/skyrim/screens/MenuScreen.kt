package com.example.skyrim.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skyrim.R
import com.example.skyrim.navigation.Screen
import com.example.skyrim.ui.LoopingRawVideo
import com.example.skyrim.ui.screenContentPadding
import com.example.skyrim.ui.theme.SkyrimFont
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

@Composable
fun MenuScreen(navController: NavController) {
    val context = LocalContext.current

    val musicPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri("android.resource://${context.packageName}/${R.raw.menu}")
            setMediaItem(mediaItem)
            repeatMode = Player.REPEAT_MODE_ONE
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            musicPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        LoopingRawVideo(
            rawResId = R.raw.menu_bg,
            muteVideo = true,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .screenContentPadding(extraTop = 0.dp, extraHorizontal = 20.dp, extraBottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Elevated Title Section (further away from central logo)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 4.dp) 
            ) {
                Text(
                    text = "RunePairs",
                    style = TextStyle(
                        fontSize = 44.sp,
                        color = Color.White,
                        fontFamily = SkyrimFont,
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color.Black,
                            blurRadius = 12f
                        )
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Skyrim Card Matching",
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontFamily = SkyrimFont,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 2.sp,
                        shadow = Shadow(Color.Black, blurRadius = 8f)
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Lower Menu Section (further away from central logo)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 20.dp) 
            ) {
                MenuButton(text = "Start Quest") {
                    navController.navigate(Screen.DifficultySelect.route)
                }
                MenuButton(text = "Catalog") {
                    navController.navigate(Screen.Gallery.route)
                }
                MenuButton(text = "Leaderboard") {
                    navController.navigate(Screen.Leaderboards.route)
                }
                MenuButton(text = "Settings") {
                    navController.navigate(Screen.Settings.route)
                }
                MenuButton(text = "Exit") {
                    (context as? Activity)?.finish()
                }
            }
        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.68f) // More compact
            .height(44.dp)      // More compact
            .background(
                color = Color.Black.copy(alpha = 0.5f), // Translucent black
                shape = RoundedCornerShape(2.dp)
            )
            .border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(2.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            style = TextStyle(
                fontSize = 16.sp, // Minimized font size
                fontWeight = FontWeight.Bold,
                color = Color.White, // White instead of yellow
                fontFamily = SkyrimFont,
                letterSpacing = 2.sp,
                shadow = Shadow(Color.Black, blurRadius = 4f)
            )
        )
    }
}
