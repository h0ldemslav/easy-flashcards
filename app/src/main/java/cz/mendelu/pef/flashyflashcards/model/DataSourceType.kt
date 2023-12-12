package cz.mendelu.pef.flashyflashcards.model

import java.io.Serializable

// `remoteId` is the only unique identifier that can have both businesses, from API and local DB
// While id of business is not, for instance when fetching data, records won't have an id (but will have remoteId)
// And they can be already stored in DB and we can get them by remoteId
sealed class DataSourceType(val remoteId: String) : Serializable {
    data class Local(private val _remoteId: String) : DataSourceType(_remoteId)
    data class Remote(private val _remoteId: String) : DataSourceType(_remoteId)
}