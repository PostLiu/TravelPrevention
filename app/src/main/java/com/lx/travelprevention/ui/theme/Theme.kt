package com.lx.travelprevention.ui.theme

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lx.travelprevention.MainActivity
import com.lx.travelprevention.common.Constant
import com.lx.travelprevention.common.DataStoreUtils
import com.lx.travelprevention.model.entity.ThemeType

private val DarkColorPalette = darkColors(
    primary = Purple200, primaryVariant = Purple700, secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500, primaryVariant = Purple700, secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun TravelPreventionTheme(
    themeType: ThemeType = ThemeType.PURPLE,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colors = colors.copy(primary = Color(themeType.color)),
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}