package com.algolia.instantsearch.examples.android

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppColors {
    // The titles & background: Cosmos Black
    val cosmos = Color(0xFF21243D)
    val solstice = Color(0xFF3A416F)

    // The words and paragraphs: Telluric Grey
    val telluric = Color(0xFF5D6494)
    val nova = Color(0xFF848AB8)
    val coolGrey = Color(0xFF848AB8)

    // The canvas: moon gradient
    val proton = Color(0xFFC5C9E0)
    val moon = Color(0xFFF5F5FA)
    val white = Color(0xFFFFFFFF)
    val lavenderGray = Color(0xFFC4C8D8)

    // The accent: Nebula Blue
    val nebulaBlue = Color(0xFF5468FF)

    val mars = Color(0xFFED5A6A)
    val venus = Color(0xFFAE3E88)
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = lightColors(), content = content)
}

private fun lightColors(
    primary: Color = AppColors.cosmos,
    primaryVariant: Color = AppColors.solstice,
    secondary: Color = AppColors.telluric,
    secondaryVariant: Color = AppColors.nova,
    background: Color = AppColors.moon,
    surface: Color = Color.White,
    error: Color = AppColors.mars,
    onPrimary: Color = Color.White,
    onSecondary: Color = Color.Black,
    onBackground: Color = Color.Black,
    onSurface: Color = Color.Black,
    onError: Color = Color.White
): Colors = Colors(
    primary,
    primaryVariant,
    secondary,
    secondaryVariant,
    background,
    surface,
    error,
    onPrimary,
    onSecondary,
    onBackground,
    onSurface,
    onError,
    true
)
