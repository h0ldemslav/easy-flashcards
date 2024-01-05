package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.architecture.CommunicationResult
import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesRepository
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import cz.mendelu.pef.flashyflashcards.model.AppPreferenceConstants
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import cz.mendelu.pef.flashyflashcards.model.DataSourceType
import cz.mendelu.pef.flashyflashcards.model.DataSourceType.Local
import cz.mendelu.pef.flashyflashcards.model.DataSourceType.Remote
import cz.mendelu.pef.flashyflashcards.remote.CS_LOCALE
import cz.mendelu.pef.flashyflashcards.remote.EN_LOCALE
import cz.mendelu.pef.flashyflashcards.remote.YelpAPIRepository
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val yelpAPIRepository: YelpAPIRepository,
    private val businessesRepository: BusinessesRepository,
    private val dataStoreRepository: DataStoreRepository
): BaseViewModel(), DetailScreenActions {

    var uiState by mutableStateOf(UiState<Business?, ScreenErrors>())

    fun getBusiness(dataSourceType: DataSourceType) {
        uiState = UiState(
            loading = true
        )

        launch {
            val entity = businessesRepository.getBusinessByRemoteId(dataSourceType.remoteId)
            val businessFromDb = if (entity != null) {
                Business.createFromBusinessEntity(entity)
            } else {
                null
            }

            var business: Business? = null

            when (dataSourceType) {
                is Local -> {
                    business = businessFromDb
                }

                is Remote -> {
                    // Cached business comes from list of fetched businesses from the API
                    // Don't want to modify item from the list, so thus there is a copy
                    business = yelpAPIRepository.getCachedBusiness()?.copy()
                    // `whenAdded` tells, if business is bookmarked
                    business?.whenAdded = businessFromDb?.whenAdded
                    // Business must have the same values as entity, so user can delete the business
                    business?.id = businessFromDb?.id
                }
            }

            val errors =  if (business == null) {
                ScreenErrors(
                    imageRes = R.drawable.undraw_empty,
                    messageRes = R.string.something_went_wrong_error
                )
            } else {
                null
            }

            val oneDayInMilliSeconds = 86400000L
            val dateOfExpire = business?.whenAdded?.plus(oneDayInMilliSeconds)
            val currentTime = System.currentTimeMillis()

            // Refresh data every 24 hours due to API policy
            if (dateOfExpire != null && currentTime >= dateOfExpire) {
                fetchFreshBusiness(business!!)
            } else {
                uiState = UiState(
                    data = business,
                    errors = errors
                )
            }
        }
    }

    override fun saveBusiness() {
        val business = uiState.data

        if (business != null) {
            val whenAdded = System.currentTimeMillis()

            business.whenAdded = whenAdded

            launch {
                val businessEntity = BusinessEntity.createFromBusiness(business)
                val id = businessesRepository.addNewBusiness(businessEntity)

                business.id = id
            }
            
            uiState = UiState(data = business)
        }
    }

    override fun deleteBusiness() {
        val business = uiState.data

        if (business != null) {
            launch {
                val businessEntity = BusinessEntity.createFromBusiness(business)
                businessesRepository.deleteBusiness(businessEntity)

                business.whenAdded = null
                business.id = null

                uiState = UiState(
                    data = business
                )
            }
        }
    }

    private suspend fun fetchFreshBusiness(business: Business) {
        uiState = UiState(loading = true)


        dataStoreRepository.getAppPreferences().take(1).collect { preferences ->
            val language = preferences
                .find { it.name == AppPreferenceConstants.LANG }
                ?.value
            val searchLocale = when (language) {
                AppPreferenceConstants.LANG_CS -> CS_LOCALE
                else -> EN_LOCALE
            }
            val result = yelpAPIRepository.getBusinessByRemoteID(
                business.remoteId,
                searchLocale
            )

            uiState = if (result is CommunicationResult.Success) {
                val newBusiness = yelpAPIRepository.convertBusinessDTOToBusiness(result.data)
                // Don't forget to set the id and new expire date
                newBusiness.id = business.id
                newBusiness.whenAdded = System.currentTimeMillis()

                // Don't forget to update the record in db
                val businessEntity = BusinessEntity.createFromBusiness(newBusiness)
                businessesRepository.updateBusiness(businessEntity)

                UiState(data = newBusiness)
            } else {
                // Error happened, but show at least old data
                UiState(
                        data = business,
                        errors = ScreenErrors(
                            messageRes = R.string.failed_to_refresh_business_try_again
                        )
                )
            }
        }
    }
}