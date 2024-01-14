package cz.mendelu.pef.flashyflashcards.fake

import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesRepository
import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBusinessesRepositoryImpl : BusinessesRepository {
    private val businesses = mutableListOf(
        BusinessEntity(
            id = 0,
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
            whenAdded = 1705254833406
        ),
        BusinessEntity(
            id = 1,
            remoteId = "remote 1",
            name = "Moravska knihovna",
            imageUrl = "",
            category = "Libraries",
            displayAddress = "Brno",
            businessUrl = "",
            rating = "4",
            reviewCount = 2,
            latitude = 49.12,
            longitude = 16.35,
            whenAdded = 1705254833406
        ),
        BusinessEntity(
            id = 2,
            remoteId = "remote 2",
            name = "Narodni muzeum",
            imageUrl = "",
            category = "Museums",
            displayAddress = "Praha",
            businessUrl = "",
            rating = "4.5",
            reviewCount = 51,
            latitude = 50.4,
            longitude = 14.25,
            whenAdded = 1705254833406
        )
    )

    override fun getAllBusinesses(): Flow<List<BusinessEntity>> {
        return flow {
            emit(businesses)
        }
    }

    override suspend fun getBusinessByRemoteId(businessRemoteId: String): BusinessEntity? {
        return businesses.firstOrNull { it.remoteId == businessRemoteId }
    }

    override suspend fun addNewBusiness(businessEntity: BusinessEntity): Long? {
        return null
    }

    override suspend fun updateBusiness(businessEntity: BusinessEntity) {

    }

    override suspend fun deleteBusiness(businessEntity: BusinessEntity) {

    }
}