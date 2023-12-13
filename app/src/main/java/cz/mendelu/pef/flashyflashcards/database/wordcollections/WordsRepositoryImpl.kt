package cz.mendelu.pef.flashyflashcards.database.wordcollections

import cz.mendelu.pef.flashyflashcards.model.WordEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(
    private val wordsDao: WordsDao
) : WordsRepository {
    override fun getAllWords(): Flow<List<WordEntity>> {
        return wordsDao.getAllWords()
    }

    override suspend fun addNewWord(wordEntity: WordEntity) {
        wordsDao.addNewWord(wordEntity)
    }
}