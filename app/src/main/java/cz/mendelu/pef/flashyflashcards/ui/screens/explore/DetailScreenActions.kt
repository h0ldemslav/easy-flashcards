package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import com.google.android.gms.maps.model.LatLng

interface DetailScreenActions {

    fun saveBusiness()
    fun deleteBusiness()
    fun getDistance(businessLocation: LatLng, userLocation: LatLng, inKm: Boolean): Double
}