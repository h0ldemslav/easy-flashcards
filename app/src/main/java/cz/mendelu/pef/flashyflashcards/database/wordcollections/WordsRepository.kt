package cz.mendelu.pef.flashyflashcards.database.wordcollections

import cz.mendelu.pef.flashyflashcards.model.WordEntity
import kotlinx.coroutines.flow.Flow

interface WordsRepository {

    fun getAllWords(): Flow<List<WordEntity>>
    suspend fun addNewWord(wordEntity: WordEntity)
    suspend fun updateWord(wordEntity: WordEntity)
    suspend fun deleteWord(wordEntity: WordEntity)
}