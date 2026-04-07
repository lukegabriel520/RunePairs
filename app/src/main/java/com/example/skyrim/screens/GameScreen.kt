package com.example.skyrim.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skyrim.R
import com.example.skyrim.auth.LocalDataManager
import com.example.skyrim.game.GameDifficulty
import com.example.skyrim.navigation.Screen
import com.example.skyrim.ui.theme.SkyrimFont
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MemoryCard(
    val id: Int,
    val iconRes: Int,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

@Composable
fun GameScreen(navController: NavController, difficulty: GameDifficulty) {
    val context = LocalContext.current
    val config = difficulty.config
    val scope = rememberCoroutineScope()
    val dataManager = remember { LocalDataManager(context) }
    
    // Audio Player for Flip Sound
    val flipSoundPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri("android.resource://${context.packageName}/${R.raw.menu}")
            setMediaItem(mediaItem)
            prepare()
        }
    }

    // Background Music
    val musicPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri("android.resource://${context.packageName}/${R.raw.ingame}")
            setMediaItem(mediaItem)
            repeatMode = Player.REPEAT_MODE_ONE
            prepare()
            playWhenReady = dataManager.isMusicEnabled()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            musicPlayer.release()
            flipSoundPlayer.release()
        }
    }

    // City Icons for the memory game
    val cityIcons = listOf(
        R.drawable.markarth, R.drawable.falkreath, R.drawable.whiterun,
        R.drawable.solitude, R.drawable.windhelm, R.drawable.riften,
        R.drawable.winterhold, R.drawable.dawnstar, R.drawable.morthal
    )

    // Game State
    var cards by remember {
        mutableStateOf(
            (cityIcons.take(config.pairCount) + cityIcons.take(config.pairCount))
                .shuffled()
                .mapIndexed { index, icon -> MemoryCard(index, icon) }
        )
    }
    
    var flippedIndices by remember { mutableStateOf(listOf<Int>()) }
    var timeLeft by remember { mutableIntStateOf(config.timeLimitSeconds) }
    var score by remember { mutableIntStateOf(0) }
    var flips by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    // Click handler
    val onCardClick: (Int) -> Unit = { index ->
        val card = cards[index]
        if (flippedIndices.size < 2 && !card.isFlipped && !card.isMatched) {
            if (dataManager.isSoundEnabled()) {
                flipSoundPlayer.seekTo(0)
                flipSoundPlayer.playWhenReady = true
            }
            
            val newCards = cards.toMutableList()
            newCards[index] = card.copy(isFlipped = true)
            cards = newCards
            flippedIndices = flippedIndices + index

            if (flippedIndices.size == 2) {
                flips++ // Increment based on pairs flipped
                scope.launch {
                    delay(800L)
                    val firstIndex = flippedIndices[0]
                    val secondIndex = flippedIndices[1]
                    
                    if (cards[firstIndex].iconRes == cards[secondIndex].iconRes) {
                        val matchedCards = cards.toMutableList()
                        matchedCards[firstIndex] = matchedCards[firstIndex].copy(isMatched = true)
                        matchedCards[secondIndex] = matchedCards[secondIndex].copy(isMatched = true)
                        cards = matchedCards
                        score += 100
                    } else {
                        val hiddenCards = cards.toMutableList()
                        hiddenCards[firstIndex] = hiddenCards[firstIndex].copy(isFlipped = false)
                        hiddenCards[secondIndex] = hiddenCards[secondIndex].copy(isFlipped = false)
                        cards = hiddenCards
                    }
                    flippedIndices = emptyList()
                }
            }
        }
    }

    // Timer Logic
    LaunchedEffect(key1 = isGameOver) {
        while (timeLeft > 0 && !isGameOver) {
            delay(1000L)
            timeLeft--
            if (timeLeft == 0) {
                isGameOver = true
                navController.navigate(Screen.GameOver.createRoute(false, score, config.timeLimitSeconds, flips, difficulty.name))
            }
        }
    }

    // Win Logic
    LaunchedEffect(cards) {
        if (cards.all { it.isMatched } && cards.isNotEmpty()) {
            isGameOver = true
            val bonus = (timeLeft * 10)
            val finalScore = score + bonus
            navController.navigate(Screen.GameOver.createRoute(true, finalScore, config.timeLimitSeconds - timeLeft, flips, difficulty.name))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val headerPadding = if (difficulty == GameDifficulty.HELL) 24.dp else 40.dp
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = headerPadding, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatBox(label = "TIME", value = timeLeft.toString())
                GameStatBox(label = "SCORE", value = score.toString())
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (difficulty == GameDifficulty.CASUAL && cards.size == 8) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxHeight(0.85f),
                        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)) {
                            for (i in 0..2) Box(modifier = Modifier.weight(1f)) { MemoryCardItem(cards[i], 0.75f) { onCardClick(i) } }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)) {
                            for (i in 3..5) Box(modifier = Modifier.weight(1f)) { MemoryCardItem(cards[i], 0.75f) { onCardClick(i) } }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)) {
                            Spacer(modifier = Modifier.weight(0.5f))
                            Box(modifier = Modifier.weight(1f)) { MemoryCardItem(cards[6], 0.75f) { onCardClick(6) } }
                            Box(modifier = Modifier.weight(1f)) { MemoryCardItem(cards[7], 0.75f) { onCardClick(7) } }
                            Spacer(modifier = Modifier.weight(0.5f))
                        }
                    }
                } else if (difficulty == GameDifficulty.HELL && cards.size == 18) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxHeight(0.9f),
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (row in 0..3) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                            ) {
                                for (col in 0..3) {
                                    val index = row * 4 + col
                                    Box(modifier = Modifier.weight(1f)) {
                                        MemoryCardItem(cards[index], 0.85f) { onCardClick(index) }
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Box(modifier = Modifier.weight(1f)) { MemoryCardItem(cards[16], 0.85f) { onCardClick(16) } }
                            Box(modifier = Modifier.weight(1f)) { MemoryCardItem(cards[17], 0.85f) { onCardClick(17) } }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                } else {
                    val columns = config.gridColumns
                    val cardAspectRatio = 0.75f
                    val verticalSpacing = 12.dp
                    val horizontalSpacing = 12.dp
                    val gridPadding = 20.dp

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        modifier = Modifier
                            .padding(horizontal = gridPadding)
                            .fillMaxHeight(if (difficulty == GameDifficulty.ADEPT) 0.95f else 0.85f),
                        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
                        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
                        contentPadding = PaddingValues(bottom = 8.dp, top = 8.dp)
                    ) {
                        itemsIndexed(cards) { index, card ->
                            MemoryCardItem(card, cardAspectRatio) { onCardClick(index) }
                        }
                    }
                }
            }

            // Flips counter
            Text(
                text = "Flips: $flips",
                color = Color(0xFFD4AF37),
                fontSize = 18.sp,
                fontFamily = SkyrimFont,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Footer - Pause Button
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable { 
                        musicPlayer.pause()
                        navController.navigate(Screen.Pause.route) 
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pause),
                    contentDescription = "Pause",
                    modifier = Modifier.size(if (difficulty == GameDifficulty.HELL) 44.dp else 56.dp)
                )
            }
        }
    }
}

@Composable
fun MemoryCardItem(card: MemoryCard, aspectRatio: Float, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped || card.isMatched) 180f else 0f,
        animationSpec = tween(
            durationMillis = 600, 
            easing = LinearOutSlowInEasing
        ),
        label = "cardRotation"
    )

    Box(
        modifier = Modifier
            .aspectRatio(aspectRatio)
            .shadow(
                elevation = 16.dp, 
                shape = RoundedCornerShape(6.dp),
                clip = false
            )
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
                
                val scale = if (card.isFlipped && !card.isMatched) 1.05f else 1f
                scaleX = scale
                scaleY = scale
            }
            .clickable { onClick() }
    ) {
        if (rotation <= 90f) {
            Image(
                painter = painterResource(id = R.drawable.card_back),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent, RoundedCornerShape(6.dp)),
                contentScale = ContentScale.FillBounds
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
                    .background(Color.White, RoundedCornerShape(6.dp))
                    .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = card.iconRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.85f),
                    contentScale = ContentScale.Fit
                )
            }
        }
        
        if (card.isMatched) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {}
        }
    }
}

@Composable
fun GameStatBox(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            color = Color(0xFFD4AF37),
            fontSize = 14.sp,
            fontFamily = SkyrimFont,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = SkyrimFont,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
