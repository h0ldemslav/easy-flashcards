package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.wordcollections.TestHistoryRepository
import cz.mendelu.pef.flashyflashcards.model.TestHistory
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestHistoryScreenViewModel @Inject constructor(
    private val testHistoryRepository: TestHistoryRepository
) : BaseViewModel() {

    var uiState by mutableStateOf(UiState<List<TestHistory>, ScreenErrors>())

    fun getAllTestHistoryByCollectionId(collectionId: Long?) {
        uiState = UiState(loading = true)

        launch {
            testHistoryRepository.getAllTestHistoryByCollectionId(collectionId)
                .collect { entities ->
                    val listOfTestHistory = entities
                        .map { TestHistory.createFromTestHistoryEntity(it) }
                        .sortedByDescending { it.dateOfCompletion }

                    uiState = UiState(data = listOfTestHistory)
                }
        }
    }
}