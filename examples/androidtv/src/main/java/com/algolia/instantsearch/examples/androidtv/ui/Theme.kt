package com.algolia.instantsearch.examples.androidtv.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White

@Composable
fun AppTheme(content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = ColorPalette,
        content = content
    )
}

private val ColorPalette = darkColors(
    primary = blue200,
    primaryVariant = blue,
    secondary = teal200,
    background = Black,
    surface = Black,
    onPrimary = Black,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    error = errorColor
)
