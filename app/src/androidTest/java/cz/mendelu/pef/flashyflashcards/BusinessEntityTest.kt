package cz.mendelu.pef.flashyflashcards

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import cz.mendelu.pef.flashyflashcards.database.FlashyFlashcardsDatabase
import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesDao
import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class BusinessEntityTest {
    private val businessEntity = BusinessEntity(
        remoteId = "remote 0",
        name = "Vasamuseet",
        imageUrl = "",
        category = "Museums",
        displayAddress = "Stockholm",
        businessUrl = "",
        rating = "4.5",
        reviewCount = 39,
        latitude = 59.32811,
        longitude = 18.09139,
        whenAdded = 0L
    )
    private lateinit var dao: BusinessesDao
    private lateinit var db: FlashyFlashcardsDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            FlashyFlashcardsDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.businessesDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testAddNewBusiness() = runBlocking {
        val _businessEntity = businessEntity.copy()
        val id = dao.addNewBusiness(_businessEntity)
        assert(id != null)

        _businessEntity.id = id

        val savedBusinessEntity = dao.getBusinessByRemoteId(_businessEntity.remoteId)
        assert(savedBusinessEntity == _businessEntity)
    }

    @Test
    fun testUpdateEntity() = runBlocking {
        val _businessEntity = businessEntity
        val id = dao.addNewBusiness(_businessEntity)
        assert(id != null)

        _businessEntity.id = id
        _businessEntity.name = "Narodni muzeum"
        _businessEntity.displayAddress = "Praha"
        _businessEntity.latitude = 50.4
        _businessEntity.longitude = 14.25

        dao.updateBusiness(_businessEntity)

        val updatedBusinessEntity = dao.getBusinessByRemoteId(_businessEntity.remoteId)
        assert(updatedBusinessEntity == _businessEntity)
    }

    @Test
    fun testDeleteEntity() = runBlocking {
        val _businessEntity = businessEntity
        val id = dao.addNewBusiness(_businessEntity)
        assert(id != null)

        _businessEntity.id = id

        dao.deleteBusiness(_businessEntity)

        val deletedEntity = dao.getBusinessByRemoteId(_businessEntity.remoteId)
        assert(deletedEntity == null)
    }
}