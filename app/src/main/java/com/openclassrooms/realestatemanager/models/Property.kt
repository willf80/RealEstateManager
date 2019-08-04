package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

import java.util.Date

@Entity(indices = [Index("propertyTypeId"), Index("userId")],
        foreignKeys = [
            ForeignKey(entity = PropertyType::class, parentColumns = ["id"], childColumns = ["propertyTypeId"]),
            ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
        ])
class Property {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var price: Double = 0.toDouble()
    var area: Double = 0.toDouble()
    var numberOfRooms: Int = 0
    var numberOfBathrooms: Int = 0
    var numberOfBedrooms: Int = 0
    var description: String? = null
    var addressLine2: String? = null
    var isSold: Boolean = false
    var createdDate: Date? = null
    var modifiedDate: Date? = null
    var soldDate: Date? = null
    var propertyTypeId: Long = 0 //FK
    var userId: Long = 0 //FK
}
