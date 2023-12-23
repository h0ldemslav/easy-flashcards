package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.wordcollections.TestHistoryRepository
import cz.mendelu.pef.flashyflashcards.model.TestHistory
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.entities.TestHistoryEntity
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestHistoryDetailScreenViewModel @Inject constructor(
    private val testHistoryRepository: TestHistoryRepository
) : BaseViewModel() {

    var uiState by mutableStateOf(UiState<TestHistory, ScreenErrors>())

    fun getTestHistoryById(testHistoryId: Long?) {
        uiState = UiState(loading = true)

        launch {
            val entity = testHistoryRepository.getSingleTestHistoryById(testHistoryId)

            if (entity != null) {
                val testHistory = TestHistory.createFromTestHistoryEntity(entity)

                uiState = UiState(data = testHistory)
            } else {
                uiState = UiState(
                    errors = ScreenErrors(
                        imageRes = null,
                        messageRes = R.string.failed_to_fetch_test_history
                    )
                )
            }
        }
    }

    fun deleteTestHistory() {
        uiState.data?.let { history ->
            launch {
                val testHistoryEntity = TestHistoryEntity.createFromTestHistory(history)

                testHistoryRepository.deleteTestHistory(testHistoryEntity)
            }
        }
    }
}