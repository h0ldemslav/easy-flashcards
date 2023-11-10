package cz.mendelu.pef.flashyflashcards.ui.activities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.ramcosta.composedestinations.spec.NavGraphSpec
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import cz.mendelu.pef.flashyflashcards.ui.screens.NavGraphs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : BaseViewModel() {

    val startNavGraph: MutableState<NavGraphSpec> = mutableStateOf(NavGraphs.root)
    val isLoading: MutableState<Boolean> = mutableStateOf(true)

    init {
        launch {
            dataStoreRepository.isOnboardingFinished().collect { onBoardingState ->
                if (onBoardingState) {
                    startNavGraph.value = NavGraphs.collections
                }

                isLoading.value = false
            }
        }
    }
}