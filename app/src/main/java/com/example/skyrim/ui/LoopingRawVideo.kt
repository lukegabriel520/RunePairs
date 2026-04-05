package com.example.skyrim.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView

/**
 * Full-bleed looping video from [res/raw][rawResId] (e.g. menu_bg.mp4 → R.raw.menu_bg).
 * When [muteVideo] is true, audio from the file is silenced so a separate BGM player can be used.
 */
@Composable
fun LoopingRawVideo(
    rawResId: Int,
    modifier: Modifier = Modifier,
    muteVideo: Boolean = true
) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build()
    }

    DisposableEffect(rawResId, muteVideo) {
        player.stop()
        player.setMediaItem(
            MediaItem.fromUri("android.resource://${context.packageName}/$rawResId")
        )
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.volume = if (muteVideo) 0f else 1f
        player.prepare()
        player.playWhenReady = true
        onDispose { }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                this.player = player
            }
        },
        modifier = modifier,
        update = { it.player = player }
    )
}
