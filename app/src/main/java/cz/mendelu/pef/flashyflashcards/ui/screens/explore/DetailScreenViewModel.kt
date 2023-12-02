package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.DataSourceType
import cz.mendelu.pef.flashyflashcards.remote.YelpAPIRepository
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val yelpAPIRepository: YelpAPIRepository
): BaseViewModel() {

    var uiState by mutableStateOf(UiState<Business?, ScreenErrors>())

    fun getBusiness(dataSourceType: DataSourceType) {
        val business = when (dataSourceType) {
            DataSourceType.Local -> {
                TODO()
            }

            DataSourceType.Remote -> {
                yelpAPIRepository.getCachedBusiness()
            }
        }

        val errors = if (business == null) {
            ScreenErrors(
                imageRes = R.drawable.undraw_empty,
                messageRes = R.string.something_went_wrong_error
            )
        } else {
            null
        }

        uiState = UiState(
            data = business,
            errors = errors
        )
    }
}