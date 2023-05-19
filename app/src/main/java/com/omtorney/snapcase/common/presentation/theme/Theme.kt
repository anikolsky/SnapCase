package com.omtorney.snapcase.common.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorPalette = darkColors(
    primary = PastelPeach,
    primaryVariant = Purple700,
    secondary = Teal200

//    primary = Grey4,
//    primaryVariant = Grey3,
//    secondary = RallyYellow,
//    secondaryVariant = RallyGreen,
//    background = Grey5,
//    onPrimary = Grey1,
//    onSecondary = Grey1,
//    onBackground = Grey1
)

private val LightColorPalette = lightColors(
    primary = Mariner1,
    primaryVariant = Purple700,
    secondary = Teal200

//    primary = Grey2,
//    primaryVariant = Grey1,
//    secondary = RallyDarkGreen,
//    secondaryVariant = RallyDarkBlue,
//    background = Color.White,
//    onPrimary = Color.Black,
//    onSecondary = Color.Black,
//    onBackground = Color.Black

    /* Other default colors to override
    surface = Color.White,
    onSurface = Color.Black,
    */
)

@Composable
fun SnapCaseTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}