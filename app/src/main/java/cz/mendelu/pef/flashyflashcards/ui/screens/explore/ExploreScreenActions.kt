package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import cz.mendelu.pef.flashyflashcards.model.Business

interface ExploreScreenActions {

    fun updateScreenData(data: ExploreScreenData)
    fun searchPlaces()
    fun getAnotherPlaces()
    fun cacheBusiness(business: Business)
}