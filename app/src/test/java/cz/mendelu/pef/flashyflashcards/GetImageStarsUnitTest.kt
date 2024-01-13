package cz.mendelu.pef.flashyflashcards

import cz.mendelu.pef.flashyflashcards.extensions.getImageStarsResFromFloat
import org.junit.Test

class GetImageStarsUnitTest {

    @Test
    fun testStringsContainOnlyDigits() {
        val zeroStars = "0.55".getImageStarsResFromFloat()
        val oneStar = "1.2".getImageStarsResFromFloat()
        val twoAndHalfStars = "2.75".getImageStarsResFromFloat()
        val fiveStars = "5".getImageStarsResFromFloat()

        assert(zeroStars == R.drawable.stars_small_0)
        assert(oneStar == R.drawable.stars_small_1)
        assert(twoAndHalfStars == R.drawable.stars_small_2_half)
        assert(fiveStars == R.drawable.stars_small_5)
    }

    @Test
    fun testStringsContainNotOnlyDigits() {
        val zeroStarsFromLetters = "xyz".getImageStarsResFromFloat()
        val zeroStarsFromDigitsWithMultiplePeriods = "2..0.1".getImageStarsResFromFloat()
        val zeroStarsFromSpecialCharacters = "@#_$".getImageStarsResFromFloat()
        val zeroStarsFromDigitsWithMinusSign = "-1.5".getImageStarsResFromFloat()

        assert(zeroStarsFromLetters == R.drawable.stars_small_0)
        assert(zeroStarsFromDigitsWithMultiplePeriods == R.drawable.stars_small_0)
        assert(zeroStarsFromSpecialCharacters == R.drawable.stars_small_0)
        assert(zeroStarsFromDigitsWithMinusSign == R.drawable.stars_small_0)
    }
}