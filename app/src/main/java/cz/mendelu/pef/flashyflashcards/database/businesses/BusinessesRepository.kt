package cz.mendelu.pef.flashyflashcards.database.businesses

import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import kotlinx.coroutines.flow.Flow

interface BusinessesRepository {

    fun getAllBusinesses(): Flow<List<BusinessEntity>>
    suspend fun getBusinessByRemoteId(businessRemoteId: String): BusinessEntity?
    suspend fun addNewBusiness(businessEntity: BusinessEntity): Long?
    suspend fun updateBusiness(businessEntity: BusinessEntity)
    suspend fun deleteBusiness(businessEntity: BusinessEntity)
}