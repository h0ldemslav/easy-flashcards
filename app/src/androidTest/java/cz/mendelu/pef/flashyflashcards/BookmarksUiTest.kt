package cz.mendelu.pef.flashyflashcards

import androidx.activity.compose.setContent
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesRepository
import cz.mendelu.pef.flashyflashcards.fake.FakeBusinessesRepositoryImpl
import cz.mendelu.pef.flashyflashcards.ui.activities.MainActivity
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.BookmarksScreen
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.BookmarksViewModel
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.TestTagBookmarksList
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.TestTagFilterTextField
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class BookmarksUiTest {

    private lateinit var navController: NavHostController
    private lateinit var viewModel: BookmarksViewModel
    private lateinit var businessesRepository: BusinessesRepository

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()

        businessesRepository = FakeBusinessesRepositoryImpl()
        viewModel = BookmarksViewModel(businessesRepository)

        composeTestRule.activity.setContent {
            navController = rememberNavController()

            BookmarksScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    @Test
    fun testFilterTextField() {
        with(composeTestRule) {
            waitForIdle()

            val filterByBusinessName = "Vasamuseet"
            onNodeWithTag(TestTagFilterTextField).performTextInput(filterByBusinessName)
            waitForIdle()

            onNodeWithTag(TestTagBookmarksList).assertExists()
            onNodeWithTag(TestTagBookmarksList)
                .onChildren()
                .filterToOne(SemanticsMatcher("") { it.layoutInfo.isAttached })

            Thread.sleep(2000)

            val filterByCategory = "Museums"
            onNodeWithTag(TestTagFilterTextField).performTextClearance()
            onNodeWithTag(TestTagFilterTextField).performTextInput(filterByCategory)
            waitForIdle()

            onNodeWithTag(TestTagBookmarksList).assertExists()
            onNodeWithTag(TestTagBookmarksList)
                .onChildren()
                .filter(SemanticsMatcher("") { it.layoutInfo.isAttached })

            Thread.sleep(2000)
        }
    }
}