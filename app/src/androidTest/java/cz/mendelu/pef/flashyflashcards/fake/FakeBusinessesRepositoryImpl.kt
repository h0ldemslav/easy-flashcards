package cz.mendelu.pef.flashyflashcards.fake

import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesRepository
import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBusinessesRepositoryImpl (var businessEntities: MutableList<BusinessEntity> = mutableListOf()) : BusinessesRepository {

    override fun getAllBusinesses(): Flow<List<BusinessEntity>> {
        return flow {
            emit(businessEntities)
        }
    }

    override suspend fun getBusinessByRemoteId(businessRemoteId: String): BusinessEntity? {
        return businessEntities.firstOrNull { it.remoteId == businessRemoteId }
    }

    override suspend fun addNewBusiness(businessEntity: BusinessEntity): Long? {
        if (businessEntity.id == null) {
            businessEntity.id = businessEntities.size.toLong()
        }
        businessEntities.add(businessEntity)

        return businessEntity.id
    }

    override suspend fun updateBusiness(businessEntity: BusinessEntity) {
        val businessToUpdate = businessEntities.firstOrNull { it.id == businessEntity.id }
        businessToUpdate?.let {
            it.remoteId = businessEntity.remoteId
            it.name = businessEntity.name
            it.imageUrl = businessEntity.imageUrl
            it.category = businessEntity.category
            it.displayAddress = businessEntity.displayAddress
            it.businessUrl = businessEntity.businessUrl
            it.rating = businessEntity.rating
            it.reviewCount = businessEntity.reviewCount
            it.latitude = businessEntity.latitude
            it.longitude = businessEntity.longitude
            it.whenAdded = businessEntity.whenAdded
        }
    }

    override suspend fun deleteBusiness(businessEntity: BusinessEntity) {
        for (index in businessEntities.indices) {
            if (businessEntities[index].id == businessEntity.id) {
                businessEntities.removeAt(index)
                break
            }
        }
    }
}