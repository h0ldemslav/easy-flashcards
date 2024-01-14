package cz.mendelu.pef.flashyflashcards

import cz.mendelu.pef.flashyflashcards.mlkit.MLKitTranslateManager
import org.junit.Test

class MlKitTranslateManagerUnitTest {

    private val mlKitTranslateManager = MLKitTranslateManager()

    @Test
    fun testSetSourceAndTargetLanguageCodes() {
        mlKitTranslateManager.setSourceAndTargetLanguageCodes("Czech", "English")
        val (source1, target1) = mlKitTranslateManager.getCurrentSourceAndTargetLanguageCodes()
        assert(source1 != null && target1 != null)

        // Input is case sensitive
        mlKitTranslateManager.setSourceAndTargetLanguageCodes("CZECH", "English")
        val (source2, target2) = mlKitTranslateManager.getCurrentSourceAndTargetLanguageCodes()
        assert(source2 == null && target2 != null)

        mlKitTranslateManager.setSourceAndTargetLanguageCodes("CzEcH", "aaa")
        val (source3, target3) = mlKitTranslateManager.getCurrentSourceAndTargetLanguageCodes()
        assert(source3 == null && target3 == null)
    }

    @Test
    fun testGetSourceAndTargetLanguageNames() {
        val sourceAndTarget1 = mlKitTranslateManager.getSourceAndTargetLanguageNames("cs", "sv")
        assert(sourceAndTarget1 != null)

        // Input is case sensitive
        val sourceAndTarget2 = mlKitTranslateManager.getSourceAndTargetLanguageNames("cS", "SK")
        assert(sourceAndTarget2 == null)

        val sourceAndTarget3 = mlKitTranslateManager.getSourceAndTargetLanguageNames("$", null)
        assert(sourceAndTarget3 == null)
    }
}