package com.example.skyrim.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skyrim.R
import com.example.skyrim.auth.LocalDataManager
import com.example.skyrim.navigation.Screen
import com.example.skyrim.ui.screenContentPadding
import com.example.skyrim.ui.theme.SkyrimFont

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val dataManager = remember { LocalDataManager(context) }
    
    var soundEnabled by remember { mutableStateOf(dataManager.isSoundEnabled()) }
    var musicEnabled by remember { mutableStateOf(dataManager.isMusicEnabled()) }

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
                .screenContentPadding(extraTop = 36.dp, extraHorizontal = 12.dp, extraBottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(24.dp))
                ) {
                    Image(
                        painter = painterResource(R.drawable.home),
                        contentDescription = "Back",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Settings",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = SkyrimFont,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.85f),
                            blurRadius = 10f
                        )
                    )
                )

                Spacer(modifier = Modifier.width(48.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(
                    text = "Game Settings",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = SkyrimFont,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )

                SettingsSection(title = "Audio") {
                    ToggleRow(
                        title = "Sound Effects",
                        description = "Enable game sound effects",
                        checked = soundEnabled,
                        onCheckedChange = { 
                            soundEnabled = it
                            dataManager.setSoundEnabled(it)
                        }
                    )
                    ToggleRow(
                        title = "Background Music",
                        description = "Enable background music",
                        checked = musicEnabled,
                        onCheckedChange = { 
                            musicEnabled = it
                            dataManager.setMusicEnabled(it)
                        }
                    )
                }

                SettingsSection(title = "Account") {
                    Button(
                        onClick = {
                            dataManager.signOut()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Menu.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2C2C2C)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Sign Out",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = SkyrimFont
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            content()
        }
    }
}

@Composable
private fun ToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xE62C2C2C), RoundedCornerShape(10.dp))
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.Switch
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.LightGray,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color.DarkGray,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.Black.copy(alpha = 0.35f)
            )
        )
    }
}
