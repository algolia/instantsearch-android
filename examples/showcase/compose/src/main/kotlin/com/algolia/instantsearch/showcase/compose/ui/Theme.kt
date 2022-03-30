package com.algolia.instantsearch.showcase.compose.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = BlueDark,
    onPrimary = White,
    secondary = White,
    onSecondary = GreyDark,
    onSurface = BlackLight,
    background = WhiteLight,
    onBackground = BlackLight,

)

@Composable
fun ShowcaseTheme(content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
