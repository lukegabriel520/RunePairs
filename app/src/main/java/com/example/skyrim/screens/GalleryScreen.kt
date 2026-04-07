package com.example.skyrim.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skyrim.R
import com.example.skyrim.navigation.Screen
import com.example.skyrim.ui.screenContentPadding
import com.example.skyrim.ui.theme.SkyrimFont
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

data class City(
    val name: String,
    val jarl: String,
    val environment: String,
    val description: String,
    val iconRes: Int,
    val imageRes: Int
)

@Composable
fun GalleryScreen(navController: NavController) {
    val context = LocalContext.current
    val cities = remember {
        listOf(
            City("Markarth", "Igmund", "Rocky mountains, Dwemer stone structures", "A stone-carved city filled with ancient history and political tension.", R.drawable.markarth, R.drawable.cmarkarth),
            City("Falkreath", "Siddgeir", "Dense forests, dark woods", "A quiet hold known for its massive graveyard and somber atmosphere.", R.drawable.falkreath, R.drawable.cfalkreath),
            City("Whiterun", "Balgruuf the Greater", "Plains and tundra", "A central hub of trade and commerce, known for Dragonsreach.", R.drawable.whiterun, R.drawable.cwhiterun),
            City("Solitude", "Elisif the Fair", "Coastal mountains", "Capital of Skyrim, seat of the High King and the Imperial Legion.", R.drawable.solitude, R.drawable.csolitude),
            City("Windhelm", "Ulfric Stormcloak", "Snowy tundra", "Oldest city in Skyrim, capital of Eastmarch and the Stormcloaks.", R.drawable.windhelm, R.drawable.cwindhelm),
            City("Riften", "Laila Law-Giver", "Autumn forests", "A city built on wooden platforms over water, home to the Thieves Guild.", R.drawable.riften, R.drawable.criften),
            City("Winterhold", "Korir", "Frozen coast", "Once a great city, now mostly destroyed. Home to the College of Winterhold.", R.drawable.winterhold, R.drawable.cwinterhold),
            City("Dawnstar", "Skald the Elder", "Rocky coast", "Northern port city with rich mines and a cold, harsh climate.", R.drawable.dawnstar, R.drawable.cdawnstar),
            City("Morthal", "Sorli the Builder", "Swampy marshes", "Small hold capital surrounded by mysterious swamps.", R.drawable.morthal, R.drawable.cmorthal)
        )
    }

    var selectedIndex by remember { mutableIntStateOf(0) }
    val selectedCity = cities[selectedIndex]

    // Audio setup
    val exoPlayer = remember {
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
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.other_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Scroll Overlay
        Image(
            painter = painterResource(id = R.drawable.scroll_overlay),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .screenContentPadding(extraTop = 24.dp, extraHorizontal = 24.dp, extraBottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with Home Button and Catalog Title
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { navController.navigate(Screen.Menu.route) },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(44.dp)
                        .background(Color.LightGray.copy(alpha = 0.5f), CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                ) {
                    // Using card_back.png as the Home button icon
                    Image(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = "Catalog",
                    fontSize = 40.sp,
                    color = Color.Black,
                    fontFamily = SkyrimFont,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // City Name and Icon aligned: Name -> Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Increased width from 160.dp to 220.dp to fit "Winterhold" on one line
                Box(modifier = Modifier.width(220.dp), contentAlignment = Alignment.CenterEnd) {
                    Text(
                        text = selectedCity.name,
                        fontSize = 34.sp,
                        color = Color.Black,
                        fontFamily = SkyrimFont,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1
                    )
                }
                // Increased width from 80.dp to 140.dp to keep the icon in the same relative screen position
                Box(modifier = Modifier.width(80.dp), contentAlignment = Alignment.CenterStart) {
                    Image(
                        painter = painterResource(id = selectedCity.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // City Image with strict black border
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.8f)
                    .border(2.dp, Color.Black)
            ) {
                Image(
                    painter = painterResource(id = selectedCity.imageRes),
                    contentDescription = selectedCity.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.5.dp)
                    .background(Color.Black)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // City Details layout: Label -> description
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CatalogDetailRow("Jarl:", selectedCity.jarl)
                CatalogDetailRow("Environment:", selectedCity.environment)
                CatalogDetailRow("Description:", selectedCity.description)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Navigation Arrows at the very bottom corners
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_prev),
                    contentDescription = "Previous",
                    modifier = Modifier
                        .size(56.dp)
                        .clickable { if (selectedIndex > 0) selectedIndex-- else selectedIndex = cities.size - 1 }
                        .padding(4.dp),
                    alpha = 1f
                )

                Image(
                    painter = painterResource(id = R.drawable.arrow_next),
                    contentDescription = "Next",
                    modifier = Modifier
                        .size(56.dp)
                        .clickable { if (selectedIndex < cities.size - 1) selectedIndex++ else selectedIndex = 0 }
                        .padding(4.dp),
                    alpha = 1f
                )
            }
        }
    }
}

@Composable
fun CatalogDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 17.sp,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value,
            fontSize = 17.sp,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
    }
}
