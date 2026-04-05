package com.example.skyrim.screens

import android.app.Activity
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
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
            // Elevated Title Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 2.dp) // Pushed up further
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
                            blurRadius = 15f
                        )
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Lower Menu Section (further down)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 32.dp) 
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
            .fillMaxWidth(0.65f) // More compact
            .height(42.dp)      // More compact
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(2.dp))
            .background(
                color = Color.Black.copy(alpha = 0.55f), // Translucent black
                shape = RoundedCornerShape(2.dp)
            )
            .border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(2.dp))
            .clip(RoundedCornerShape(2.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Realistic crack and weathered effect using Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw subtle crack lines
            val crackColor = Color.White.copy(alpha = 0.1f)
            val crackPath = Path().apply {
                moveTo(0f, size.height * 0.2f)
                lineTo(size.width * 0.15f, size.height * 0.25f)
                lineTo(size.width * 0.2f, 0f)

                moveTo(size.width * 0.8f, size.height)
                lineTo(size.width * 0.85f, size.height * 0.7f)
                lineTo(size.width, size.height * 0.65f)
            }
            drawPath(crackPath, crackColor, style = Stroke(width = 1f))

            // Subtle "moss/dirt" spots
            drawCircle(
                color = Color(0xFF2E3B23).copy(alpha = 0.15f),
                radius = 12f,
                center = Offset(size.width * 0.05f, size.height * 0.85f)
            )
            drawCircle(
                color = Color(0xFF2E3B23).copy(alpha = 0.1f),
                radius = 8f,
                center = Offset(size.width * 0.92f, size.height * 0.15f)
            )
        }

        Text(
            text = text.uppercase(),
            style = TextStyle(
                fontSize = 15.sp, // Minimized font size
                fontWeight = FontWeight.Bold,
                color = Color.White, // White font
                fontFamily = SkyrimFont,
                letterSpacing = 2.sp,
                shadow = Shadow(Color.Black, blurRadius = 4f)
            )
        )
    }
}
