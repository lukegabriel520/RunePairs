package com.example.skyrim.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Edge-to-edge safe area (status bar, cutout, nav bar) plus extra spacing so titles clear punch-hole cameras.
 */
@Composable
fun Modifier.screenContentPadding(
    extraTop: Dp = 16.dp,
    extraHorizontal: Dp = 16.dp,
    extraBottom: Dp = 12.dp
): Modifier =
    this
        .safeDrawingPadding()
        .padding(horizontal = extraHorizontal)
        .padding(top = extraTop, bottom = extraBottom)
