package cz.mendelu.pef.flashyflashcards.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    fun isOnboardingFinished(): Flow<Boolean>
    fun getTestAnswerLength(): Flow<Long>
    suspend fun setOnboardingFinished()
}