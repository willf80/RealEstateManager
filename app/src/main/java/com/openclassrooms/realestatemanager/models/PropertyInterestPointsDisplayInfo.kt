package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Relation

class PropertyInterestPointsDisplayInfo {
    @Embedded
    var propertyInterestPoints: PropertyInterestPoints? = null

    @Relation(parentColumn = "interestId", entityColumn = "id", entity = InterestPoint::class)
    var interestPoints: List<InterestPoint> = ArrayList()
}