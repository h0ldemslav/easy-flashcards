package cz.mendelu.pef.flashyflashcards.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(
    private val context: Context
) : DataStoreRepository {
    override suspend fun setOnboardingFinished() {
        val onBoardingKey = booleanPreferencesKey(DataStoreConstants.ONBOARDING_FINISHED)

        context.dataStore.edit { preferences ->
            if (!preferences.contains(onBoardingKey)) {
                preferences[onBoardingKey] = true
            }
        }
    }

    override fun isOnboardingFinished(): Flow<Boolean> {
        val onBoardingKey = booleanPreferencesKey(DataStoreConstants.ONBOARDING_FINISHED)

        return context.dataStore.data.map { preferences ->
            preferences[onBoardingKey] ?: false
        }
    }

    override fun getTestAnswerLength(): Flow<Long> {
        val testAnswerLengthKey = longPreferencesKey(DataStoreConstants.TEST_ANSWER_LENGTH)

        return context.dataStore.data.map { preferences ->
            preferences[testAnswerLengthKey] ?: DataStoreConstants.DEFAULT_TEST_ANSWER_LENGTH_VALUE
        }
    }
}