package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation

class PropertyAllDisplayedInfo {

    @Embedded
    var property: Property? = null

    @Relation(parentColumn = "id", entityColumn = "propertyId")
    var mediaList: List<Media> = ArrayList()

    @Ignore
    var propertyType: PropertyType? = null

    @Ignore
    var user: User? = null

    @Ignore
    var address: Address? = null

    @Ignore
    var mediaTemp: MediaTemp? = null

    @Ignore
    var interestPoints: List<InterestPoint> = ArrayList()
}