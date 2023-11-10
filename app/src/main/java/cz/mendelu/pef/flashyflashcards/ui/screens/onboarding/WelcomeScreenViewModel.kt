package cz.mendelu.pef.flashyflashcards.ui.screens.onboarding

import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : BaseViewModel() {

    fun getOnBoardingPages(): List<OnBoardingPage> {
        return listOf(
            OnBoardingPage.Welcome,
            OnBoardingPage.Collections,
            OnBoardingPage.Translation
        )
    }

    fun setOnboardingFinished() {
        launch {
            dataStoreRepository.setOnboardingFinished()
        }
    }
}