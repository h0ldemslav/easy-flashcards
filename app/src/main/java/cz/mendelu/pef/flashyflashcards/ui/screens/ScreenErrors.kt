package cz.mendelu.pef.flashyflashcards.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

open class ScreenErrors(
    @DrawableRes val imageRes: Int? = null,
    @StringRes val messageRes: Int
)
