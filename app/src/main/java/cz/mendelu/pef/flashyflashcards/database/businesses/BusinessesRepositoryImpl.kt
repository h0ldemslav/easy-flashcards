package cz.mendelu.pef.flashyflashcards.database.businesses

import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BusinessesRepositoryImpl @Inject constructor(
    private val businessesDao: BusinessesDao
) : BusinessesRepository {
    override fun getAllBusinesses(): Flow<List<BusinessEntity>> {
        return businessesDao.getAllBusinesses()
    }

    override suspend fun getBusinessByRemoteId(businessRemoteId: String): BusinessEntity? {
        return businessesDao.getBusinessByRemoteId(businessRemoteId)
    }

    override suspend fun addNewBusiness(businessEntity: BusinessEntity): Long? {
        return businessesDao.addNewBusiness(businessEntity)
    }

    override suspend fun updateBusiness(businessEntity: BusinessEntity) {
        businessesDao.updateBusiness(businessEntity)
    }

    override suspend fun deleteBusiness(businessEntity: BusinessEntity) {
        businessesDao.deleteBusiness(businessEntity)
    }
}