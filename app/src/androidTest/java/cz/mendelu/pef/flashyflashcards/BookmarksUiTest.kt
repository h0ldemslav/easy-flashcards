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
import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
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

    val businessEntities = mutableListOf(
        BusinessEntity(
            id = 0,
            remoteId = "remote 0",
            name = "Vasamuseet",
            imageUrl = "",
            category = "Museums",
            displayAddress = "Stockholm",
            businessUrl = "",
            rating = "4.5",
            reviewCount = 39,
            latitude = 59.32811,
            longitude = 18.09139,
            whenAdded = 1705254833406
        ),
        BusinessEntity(
            id = 1,
            remoteId = "remote 1",
            name = "Moravska knihovna",
            imageUrl = "",
            category = "Libraries",
            displayAddress = "Brno",
            businessUrl = "",
            rating = "4",
            reviewCount = 2,
            latitude = 49.12,
            longitude = 16.35,
            whenAdded = 1705254833406
        ),
        BusinessEntity(
            id = 2,
            remoteId = "remote 2",
            name = "Narodni muzeum",
            imageUrl = "",
            category = "Museums",
            displayAddress = "Praha",
            businessUrl = "",
            rating = "4.5",
            reviewCount = 51,
            latitude = 50.4,
            longitude = 14.25,
            whenAdded = 1705254833406
        )
    )

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

        businessesRepository = FakeBusinessesRepositoryImpl(businessEntities)
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