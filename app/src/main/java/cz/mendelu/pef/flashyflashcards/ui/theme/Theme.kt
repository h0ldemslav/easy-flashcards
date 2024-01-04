package cz.mendelu.pef.flashyflashcards.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xffffafd6),
    secondary = Color(0xffe0bdcc),
    tertiary = Color(0xfff3bb9a),
    background = Color(0xff1f1a1c),
    surface = Color(0xff1f1a1c),
    onPrimary = Color(0xff5b1140),
    onSecondary = Color(0xff402a35),
    onTertiary = Color(0xff4a2811),
    onBackground = Color(0xffebe0e2),
    onSurface = Color(0xffebe0e2),
    primaryContainer = Color(0xff772957),
    onPrimaryContainer = Color(0xffffd8e8)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xff944170),
    secondary = Color(0xff725763),
    tertiary = Color(0xff7f543a),
    background = Color(0xfffffbff),
    surface = Color(0xfffffbff),
    onPrimary = Color(0xffffffff),
    onSecondary = Color(0xffffffff),
    onTertiary = Color(0xffffffff),
    onBackground = Color(0xff1f1a1c),
    onSurface = Color(0xff1f1a1c),
    primaryContainer = Color(0xffffd8e8),
    onPrimaryContainer = Color(0xff3c0028)
)

@Composable
fun FlashyFlashcardsTheme(
    darkTheme: Boolean,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}