package cz.mendelu.pef.flashyflashcards.database.wordcollections

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cz.mendelu.pef.flashyflashcards.model.entities.TestHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TestHistoryDao {

    @Query("SELECT * FROM test_history WHERE wordCollectionId=:id")
    fun getAllTestHistoryByCollectionId(id: Long?): Flow<List<TestHistoryEntity>>

    @Query("SELECT * FROM test_history WHERE id=:testHistoryId")
    suspend fun getSingleTestHistoryById(testHistoryId: Long?): TestHistoryEntity?

    @Insert
    suspend fun createNewTestHistory(testHistoryEntity: TestHistoryEntity)

    @Delete
    suspend fun deleteTestHistory(testHistoryEntity: TestHistoryEntity)
}