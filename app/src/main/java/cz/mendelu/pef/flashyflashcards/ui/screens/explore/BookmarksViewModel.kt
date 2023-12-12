package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesRepository
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val businessesRepository: BusinessesRepository
) : BaseViewModel() {

    var uiState by mutableStateOf(UiState<List<Business>, ScreenErrors>())
    var searchInput by mutableStateOf("")

    init {
        getBusinesses()
    }

    fun filterByWord(word: String, business: Business): Boolean {
        if (word.isEmpty()) {
            return true
        }

        val displayAddressString = business.displayAddress.joinToString()

        if (business.name.contains(word) || business.category.contains(word) ||
            displayAddressString.contains(word)
        ) {
            return true
        }

        return false
    }

    private fun getBusinesses() {
        uiState = UiState(
            loading = true
        )

        launch {
            businessesRepository.getAllBusinesses().collect { entities ->
                val businesses = entities.map { Business.createFromBusinessEntity(it) }

                uiState = UiState(
                    data = businesses
                )
            }
        }
    }
}