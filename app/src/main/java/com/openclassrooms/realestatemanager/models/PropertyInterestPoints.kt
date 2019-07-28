package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(indices = [Index("propertyId"), Index("interestId")],
        primaryKeys = ["propertyId", "interestId"],
        foreignKeys = [
            ForeignKey(entity = Property::class, parentColumns = ["id"], childColumns = ["propertyId"]),
            ForeignKey(entity = InterestPoint::class, parentColumns = ["id"], childColumns = ["interestId"])
        ])
class PropertyInterestPoints(var propertyId: Long, var interestId: Long)
