package cz.mendelu.pef.flashyflashcards.extensions

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.luminance

// Thanks to Khantahr
// https://stackoverflow.com/questions/71591775/jetpack-compose-getting-light-vs-dark-mode-from-materialtheme-using-material-3

@Composable
fun ColorScheme.isDark() = this.background.luminance() < 0.5