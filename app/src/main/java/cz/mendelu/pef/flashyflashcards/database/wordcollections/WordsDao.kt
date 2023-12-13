package cz.mendelu.pef.flashyflashcards.database.wordcollections

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cz.mendelu.pef.flashyflashcards.model.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordsDao {

    @Query("SELECT * FROM word_collection_words")
    fun getAllWords(): Flow<List<WordEntity>>

    @Insert
    suspend fun addNewWord(wordEntity: WordEntity)
}