package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsRepository
import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordCollectionsViewModel @Inject constructor(
    private val wordCollectionsRepository: WordCollectionsRepository
) : BaseViewModel() {

    var uiState by mutableStateOf(UiState<MutableList<WordCollection>, ScreenErrors>(loading = true))

    init {
        getAllCollections()
    }

    private fun getAllCollections() {
        launch {
            wordCollectionsRepository.getAllWordCollections().collect {
                val wordCollections = it.map { entity ->
                    WordCollection.createFromEntity(entity)
                }.toMutableList()

                uiState = UiState(
                    loading = false,
                    data = wordCollections,
                    errors = null
                )
            }
        }
    }
}