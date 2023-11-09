package cz.mendelu.pef.flashyflashcards.ui.screens.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.flashyflashcards.R

sealed class OnBoardingPage(
    @DrawableRes val icon: Int,
    val iconSize: Dp,
    @StringRes val title: Int,
    @StringRes val body: Int
) {
    object Welcome : OnBoardingPage(
        icon = R.drawable.cards_icon,
        iconSize = 64.dp,
        title = R.string.welcome_screen_welcome_title,
        body = R.string.welcome_screen_welcome_text
    )

    object Collections : OnBoardingPage(
        icon = R.drawable.bookmark_icon,
        iconSize = 48.dp,
        title = R.string.collections,
        body = R.string.welcome_screen_collections_text
    )

    object Translation : OnBoardingPage(
        icon = R.drawable.translate_icon,
        iconSize = 48.dp,
        title = R.string.welcome_screen_translation_title,
        body = R.string.welcome_screen_translation_text
    )
}
