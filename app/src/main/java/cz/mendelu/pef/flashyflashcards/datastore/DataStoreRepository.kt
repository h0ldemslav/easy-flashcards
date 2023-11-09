package cz.mendelu.pef.flashyflashcards.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setOnboardingFinished()
    fun isOnboardingFinished(): Flow<Boolean>
}