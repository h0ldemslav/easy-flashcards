package cz.mendelu.pef.flashyflashcards.model

import androidx.annotation.StringRes
import cz.mendelu.pef.flashyflashcards.R
import java.io.Serializable

sealed class FlashcardPracticeType(@StringRes val name: Int) : Serializable {
    object Training : FlashcardPracticeType(R.string.training_label)
    data class Test(val initialTimer: Long) : FlashcardPracticeType(R.string.test_label)
}